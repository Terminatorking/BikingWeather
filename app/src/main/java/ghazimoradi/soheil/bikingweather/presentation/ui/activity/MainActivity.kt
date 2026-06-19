package ghazimoradi.soheil.bikingweather.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import ghazimoradi.soheil.bikingweather.presentation.ui.screens.WeatherScreen
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.Cosmos
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.Gray
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.Mirage
import ghazimoradi.soheil.bikingweather.presentation.ui.theme.TranquilBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = dark(Gray),
            navigationBarStyle = dark(Gray),
        )

        setContent {
            Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                WeatherScreen(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(
                            verticalGradient(
                                listOf(
                                    Mirage,
                                    Cosmos,
                                    TranquilBlack
                                )
                            )
                        )
                )
            }
        }
    }
}