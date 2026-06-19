package ghazimoradi.soheil.bikingweather.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ghazimoradi.soheil.bikingweather.domain.usecases.CalculateBikeRidingScoreUseCase
import ghazimoradi.soheil.bikingweather.domain.usecases.GetWeatherForecastUseCase

class WeatherViewModel(
    application: Application,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val calculateBikeRidingScoreUseCase: CalculateBikeRidingScoreUseCase
) : AndroidViewModel(application) {

}