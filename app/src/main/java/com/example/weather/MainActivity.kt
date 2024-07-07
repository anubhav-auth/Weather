package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.Presentation.MainScreen
import com.example.weather.Presentation.SearchScreen
import com.example.weather.Presentation.WeatherViewModel
import com.example.weather.data.Retrofitnstance
import com.example.weather.data.WeatherRepositoryImplementation
import com.example.weather.ui.theme.weatherNightBackGrad1
import com.example.weather.ui.theme.weatherNightBackGrad2
import com.example.weather.ui.theme.weatherNightBackGrad3
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : ComponentActivity() {

    private val weatherViewModel by viewModels<WeatherViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WeatherViewModel(WeatherRepositoryImplementation(Retrofitnstance.api)) as T
            }
        }
    })
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

//Initialize it where you need it


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        var allPermissionGranted by mutableStateOf(false)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                allPermissionGranted = permissions.values.all { it }
            }

        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        setContent {
            val backGroundColor = listOf(
                weatherNightBackGrad1,
                weatherNightBackGrad2,
                weatherNightBackGrad3
            )

            val navController = rememberNavController()



            Scaffold { paddingValue ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Brush.linearGradient(backGroundColor))
                        .padding(paddingValue)
                ) {
                    if (allPermissionGranted) {
                        NavHost(navController = navController, startDestination = "main_screen") {
                            composable("main_screen") {
                                MainScreen(
                                    navController = navController,
                                    fusedLocationProviderClient = fusedLocationProviderClient,
                                    viewModel = weatherViewModel
                                )
                            }
                            composable("search_screen") {
                                SearchScreen(viewModel = weatherViewModel)
                            }
                            composable("future_prediction_screen") {

                            }
                        }
                    } else {
                        //demopage PlaceHOlder
                    }
                }

            }


        }
    }
}

