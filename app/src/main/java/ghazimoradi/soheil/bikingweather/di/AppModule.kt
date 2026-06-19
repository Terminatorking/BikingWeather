package ghazimoradi.soheil.bikingweather.di

import ghazimoradi.soheil.bikingweather.data.remote.ApiService
import ghazimoradi.soheil.bikingweather.data.repository.WeatherRepositoryImpl
import ghazimoradi.soheil.bikingweather.domain.repositories.WeatherRepository
import ghazimoradi.soheil.bikingweather.domain.usecases.CalculateBikeRidingScoreUseCase
import ghazimoradi.soheil.bikingweather.domain.usecases.GetWeatherForecastUseCase
import ghazimoradi.soheil.bikingweather.presentation.viewmodel.WeatherViewModel
import ghazimoradi.soheil.bikingweather.utils.BASE_URL
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(get())
    }

    single {
        GetWeatherForecastUseCase(get())
    }

    single {
        CalculateBikeRidingScoreUseCase()
    }

    viewModel {
        WeatherViewModel(get(), get(), get())
    }

}