package ghazimoradi.soheil.bikingweather.domain.repositories

import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherResponse>
}