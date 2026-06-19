package ghazimoradi.soheil.bikingweather.presentation.ui.screens

import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ghazimoradi.soheil.bikingweather.presentation.ui.components.WeatherContent
import ghazimoradi.soheil.bikingweather.presentation.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val weatherState by viewModel.weatherState
    val locationPermissionGranted by viewModel.locationPermissionGranted

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.checkLocationPermission()
        }
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionGranted) {
            permissionLauncher.launch(ACCESS_FINE_LOCATION)
        } else {
            viewModel.checkLocationPermission()
        }
    }

    Box(modifier) {
        when {
            weatherState.isLoading && weatherState.weatherData == null -> {
                LoadingScreen()
            }

            weatherState.error != null -> {
                ErrorScreen(
                    error = weatherState.error.toString()
                ) {
                    viewModel.checkLocationPermission()
                }
            }

            weatherState.weatherData != null -> {
                WeatherContent(
                    weatherData = weatherState.weatherData!!,
                    viewModel = viewModel
                )
            }

            else -> {
                WelcomeScreen()
            }

        }
    }
}