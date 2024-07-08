package com.example.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.weather.data.ApiKey
import com.example.weather.data.model.weather.Forecastday
import com.example.weather.data.model.weather.Hour
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.Locale


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    val apiKey = ApiKey.WEATHER_API_KEY

    viewModel.getLatLang(fusedLocationProviderClient = fusedLocationProviderClient)
    val location by viewModel.latLangData.collectAsState()

    val a = location.split(",")[0] == "null"
    if (a){
        DisplayScreen(navController, viewModel, fusedLocationProviderClient, "London")
    }else{
        DisplayScreen(navController, viewModel, fusedLocationProviderClient, location)
    }


}

