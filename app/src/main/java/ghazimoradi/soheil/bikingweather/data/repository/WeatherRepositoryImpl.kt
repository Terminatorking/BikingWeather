package ghazimoradi.soheil.bikingweather.data.repository

import ghazimoradi.soheil.bikingweather.data.remote.ApiService
import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import ghazimoradi.soheil.bikingweather.domain.repositories.WeatherRepository
import ghazimoradi.soheil.bikingweather.utils.OPEN_WEATHER_API_KEY

class WeatherRepositoryImpl(private val api: ApiService) : WeatherRepository {

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherResponse> {
        return try {
            val response = api.getWeatherForecast(lat = lat, lon = lon, apiKey = OPEN_WEATHER_API_KEY)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}