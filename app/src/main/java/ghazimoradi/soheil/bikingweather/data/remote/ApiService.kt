package ghazimoradi.soheil.bikingweather.data.remote

import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): WeatherResponse

}