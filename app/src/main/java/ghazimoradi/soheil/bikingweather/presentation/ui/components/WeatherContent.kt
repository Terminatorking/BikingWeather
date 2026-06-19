package ghazimoradi.soheil.bikingweather.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import ghazimoradi.soheil.bikingweather.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherContent(
    weatherData: WeatherResponse,
    viewModel: WeatherViewModel
) {
    val dailyScores by viewModel.dailyScores
    val bestDay = remember(dailyScores) {
        dailyScores.maxByOrNull { it.second.score }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(weatherData, bestDay?.first, bestDay?.second, viewModel)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(
                items = dailyScores,
                key = { (forecast, _) -> forecast.date }
            ) { (forecast, score) ->
                BikeRidingCard(
                    forecast = forecast,
                    score = score,
                    viewModel = viewModel,
                    isBest = bestDay?.first?.date == forecast.date
                )
            }
        }
    }
}