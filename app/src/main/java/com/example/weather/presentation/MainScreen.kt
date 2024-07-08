package com.example.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.weather.data.ApiKey
import com.google.android.gms.location.FusedLocationProviderClient


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    viewModel.getLatLang(fusedLocationProviderClient = fusedLocationProviderClient)
    val location by viewModel.latLangData.collectAsState()

    val a = location.split(",")[0] == "null"
    if (a) {
        DisplayScreen(navController, viewModel, fusedLocationProviderClient, "London")
    } else {
        DisplayScreen(navController, viewModel, fusedLocationProviderClient, location)
    }


}

