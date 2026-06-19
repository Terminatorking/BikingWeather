package ghazimoradi.soheil.bikingweather.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ghazimoradi.soheil.bikingweather.domain.models.BikeRidingScore
import ghazimoradi.soheil.bikingweather.domain.models.DailyForecast
import ghazimoradi.soheil.bikingweather.domain.models.WeatherResponse
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.Cosmos
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.CrispyMintGreen
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.Oslo
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.TenderLightBlue
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.White
import ghazimoradi.soheil.bikingweather.presentation.viewmodel.WeatherViewModel

@Composable
fun HeaderSection(
    weatherData: WeatherResponse,
    bestForecast: DailyForecast?,
    bestScore: BikeRidingScore?,
    viewModel: WeatherViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Cosmos.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚴‍♂️ Bike Riding Forecast",
                color = White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weatherData.city.name}, ${weatherData.city.country}",
                color = TenderLightBlue,
                fontSize = 16.sp
            )
            if (bestForecast != null && bestScore != null) {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🏆 Best day: ",
                        color = CrispyMintGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = viewModel.formatDate(bestForecast.date),
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = bestScore.overallRating,
                    color = Oslo,
                    fontSize = 14.sp
                )
            }
        }
    }
}