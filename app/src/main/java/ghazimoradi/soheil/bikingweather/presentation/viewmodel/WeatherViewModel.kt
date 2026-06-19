package ghazimoradi.soheil.bikingweather.presentation.viewmodel

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import ghazimoradi.soheil.bikingweather.domain.models.BikeRidingScore
import ghazimoradi.soheil.bikingweather.domain.models.DailyForecast
import ghazimoradi.soheil.bikingweather.domain.models.Temperature
import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import ghazimoradi.soheil.bikingweather.domain.models.WeatherState
import ghazimoradi.soheil.bikingweather.domain.usecases.CalculateBikeRidingScoreUseCase
import ghazimoradi.soheil.bikingweather.domain.usecases.GetWeatherForecastUseCase
import ghazimoradi.soheil.bikingweather.utils.WEATHER_ICON_BASE_URL
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(
    application: Application,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val calculateBikeRidingScoreUseCase: CalculateBikeRidingScoreUseCase
) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _locationPermissionGranted = mutableStateOf(false)

    val locationPermissionGranted: State<Boolean> = _locationPermissionGranted

    private val _weatherState = mutableStateOf(WeatherState())

    val weatherState: State<WeatherState> = _weatherState

    private val _dailyScores =
        mutableStateOf<List<Pair<DailyForecast, BikeRidingScore>>>(emptyList())

    val dailyScores: State<List<Pair<DailyForecast, BikeRidingScore>>> = _dailyScores

    fun checkLocationPermission() {
        val hasPermission = checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
        _locationPermissionGranted.value = hasPermission
        if (hasPermission) {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    location?.let {
                        fetchWeatherData(it.latitude, it.longitude)
                    }
                }.addOnFailureListener {
                    _weatherState.value =
                        _weatherState.value.copy(
                            isLoading = false,
                            error = "Failed to get location: ${it.message}"
                        )
                }
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        _weatherState.value = _weatherState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            getWeatherForecastUseCase(lat = latitude, lon = longitude)
                .onSuccess { response ->
                    val dailyForecasts = processForecastIntoDaily(response)
                    val scores = dailyForecasts.map { forecast ->
                        forecast to calculateBikeRidingScoreUseCase(forecast)
                    }

                    _dailyScores.value = scores

                    _weatherState.value = _weatherState.value.copy(
                        isLoading = false,
                        weatherData = response.copy(daily = dailyForecasts),
                        error = null
                    )
                }.onFailure {
                    _weatherState.value = _weatherState.value.copy(
                        isLoading = false,
                        error = "Failed to fetch weather data: ${it.message}"
                    )
                    _dailyScores.value = emptyList()
                }
        }
    }

    private fun processForecastIntoDaily(response: WeatherResponse): List<DailyForecast> {
        val dailyForecasts = mutableListOf<DailyForecast>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        //Group weather items by data
        val dailyGroups = response.list.groupBy { item ->
            dateFormat.format(Date(item.date * 1000)) //Convert Unix timestamp (second) to milliseconds
        }.values

        dailyGroups.forEach { weatherItems ->
            if (weatherItems.isNotEmpty()) {
                val firstForecast = weatherItems.first()
                val maxTemp = weatherItems.maxOf { it.main.tempMax }
                val minTemp = weatherItems.minOf { it.main.tempMin }
                val avgHumidity = weatherItems.map { it.main.humidity }.average().toInt()
                val avgWindSpeed = weatherItems.map { it.wind.speed }.average()
                val avgPrecipitation = weatherItems.map { it.precipitationProbability }.average()

                //Get the most common weather for the day
                val mostCommonWeather = weatherItems
                    .flatMap { it.weather }
                    .groupBy { it.id }
                    .maxByOrNull { it.value.size }
                    ?.value?.first() ?: firstForecast.weather.first()

                val dailyForecast = DailyForecast(
                    date = firstForecast.date,
                    temperature = Temperature(
                        day = firstForecast.main.temp,
                        min = minTemp,
                        max = maxTemp,
                        night = firstForecast.main.temp
                    ),
                    weather = listOf(mostCommonWeather),
                    humidity = avgHumidity,
                    windSpeed = avgWindSpeed,
                    precipitationProbability = avgPrecipitation
                )

                dailyForecasts.add(dailyForecast)
            }
        }

        return dailyForecasts.take(7)
    }

    fun formatDate(timeStamp: Long): String = SimpleDateFormat(
        "EEE, MMM d",
        Locale.getDefault(),
    ).format(
        Date(timeStamp * 1000)
    )

    fun getWeatherIcon(iconCode: String): String = "$WEATHER_ICON_BASE_URL$iconCode@2x.png"
}