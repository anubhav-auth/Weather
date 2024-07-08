package com.example.weather.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Response
import com.example.weather.data.WeatherRepository
import com.example.weather.data.model.location.LocationQueryData
import com.example.weather.data.model.weather.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _latLangData = MutableStateFlow<String>("london")
    val latLangData = _latLangData.asStateFlow()

    private val _locationQueryData = MutableStateFlow<LocationQueryData?>(null)
    val locationQueryData = _locationQueryData.asStateFlow()


    private val _showErrorChannel = Channel<Boolean>()
    val showErrorChannel = _showErrorChannel.receiveAsFlow()

    @SuppressLint("MissingPermission")
    fun getLatLang(
        fusedLocationProviderClient: FusedLocationProviderClient,
        priority: Boolean = true
    ) {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY


        fusedLocationProviderClient.getCurrentLocation(accuracy, CancellationTokenSource().token)
            .addOnSuccessListener { location ->
                _latLangData.update {
                    "${location?.latitude},${location?.longitude}"
                }
            }
    }

    fun fetchWeatherData(
        key: String,
        q: String,
        days: Int = 3,
        aqi: String = "yes",
        alerts: String = "no"
    ) {
        viewModelScope.launch {
            weatherRepository.getWeatherData(key, q, days, aqi, alerts).collectLatest { result ->
                when (result) {
                    is Response.Error -> {
                        _showErrorChannel.send(true)
                    }

                    is Response.Success -> {
                        result.data?.let { data ->
                            _weatherData.update {
                                data
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchLocationQueryData(
        key: String,
        q: String
    ) {
        viewModelScope.launch {


            weatherRepository.locationQuery(key, q).collectLatest { result ->
                when (result) {
                    is Response.Error -> {
                        _showErrorChannel.send(true)
                    }

                    is Response.Success -> {
                        result.data?.let { data ->
                            _locationQueryData.update {
                                data
                            }
                        }
                    }
                }
            }
        }
    }
}