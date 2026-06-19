package ghazimoradi.soheil.bikingweather.domain.models

data class WeatherState(
    val isLoading: Boolean = false,
    val weatherData: WeatherResponse? = null,
    val error: String? = null
)