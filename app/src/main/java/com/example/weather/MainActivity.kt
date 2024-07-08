package com.example.weather

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
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
import com.example.weather.data.Retrofitnstance
import com.example.weather.data.WeatherRepositoryImplementation
import com.example.weather.presentation.DisplayScreen
import com.example.weather.presentation.FuturePredictionScreen
import com.example.weather.presentation.MainScreen
import com.example.weather.presentation.SearchScreen
import com.example.weather.presentation.WeatherViewModel
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
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

                        val isGpsEnabled =
                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                        if (!isGpsEnabled) {
                            Toast.makeText(
                                this@MainActivity,
                                "Please enable location service",
                                Toast.LENGTH_SHORT
                            ).show()
                        }




                        NavHost(navController = navController, startDestination = "main_screen") {
                            composable("main_screen") {
                                MainScreen(
                                    navController = navController,
                                    fusedLocationProviderClient = fusedLocationProviderClient,
                                    viewModel = weatherViewModel
                                )
                            }
                            composable("search_screen") {
                                SearchScreen(
                                    viewModel = weatherViewModel,
                                    navController = navController
                                )
                            }
                            composable("searched_display/{location}") {

                                val location = it.arguments?.getString("location")

                                if (location != null) {
                                    DisplayScreen(
                                        navController = navController,
                                        viewModel = weatherViewModel,
                                        fusedLocationProviderClient = fusedLocationProviderClient,
                                        location = location
                                    )
                                }
                            }
                            composable("future_prediction_screen") {
                                FuturePredictionScreen(viewModel = weatherViewModel)
                            }
                        }
                    }
                }

            }
        }
    }
}
