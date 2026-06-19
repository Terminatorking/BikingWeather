package ghazimoradi.soheil.bikingweather.domain.usecases

import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import ghazimoradi.soheil.bikingweather.domain.repositories.WeatherRepository

class GetWeatherForecastUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<WeatherResponse> {
        return repository.getWeatherForecast(lat, lon)
    }
}