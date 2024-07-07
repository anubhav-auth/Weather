package com.example.weather.Presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Response
import com.example.weather.data.Retrofitnstance.ipService
import com.example.weather.data.WeatherRepository
import com.example.weather.data.model.ipFinder.FoundIP
import com.example.weather.data.model.ipQuery.IPQuery
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
import retrofit2.http.GET
import retrofit2.http.Query

interface IpService {
    @GET("https://api.ipify.org")
    suspend fun getIp(@Query("format") format: String = "json"): FoundIP
}


class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()


    private val _locationQueryData = MutableStateFlow<LocationQueryData?>(null)
    val locationQueryData = _locationQueryData.asStateFlow()

    private val _ipQueryData = MutableStateFlow<IPQuery?>(null)
    val ipQueryData = _ipQueryData.asStateFlow()

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorChannel = _showErrorChannel.receiveAsFlow()

    @SuppressLint("MissingPermission")
    @Composable
    fun getLatLang(
        fusedLocationProviderClient: FusedLocationProviderClient,
        priority: Boolean = true
    ): String {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        var latLang by remember {
            mutableStateOf("\"51.507351,-0.127758\"")
        }

        fusedLocationProviderClient.getCurrentLocation(accuracy, CancellationTokenSource().token)
            .addOnSuccessListener { location ->
                latLang = "${location?.latitude},${location?.longitude}"
            }

        return latLang
    }

    fun fetchWeatherData(
        key: String,
        q: String,
        days: Int,
        aqi: String,
        alerts: String
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

    fun fetchLocationWithIp(
        key: String
    ) {
        viewModelScope.launch {

            val q: String = ipService.getIp().ip


            weatherRepository.ipQuery(q, key).collectLatest { result ->


                when (result) {
                    is Response.Error -> {
                        result.data?.ip?.let { Log.d("mytag", it) }
                        _showErrorChannel.send(true)
                    }

                    is Response.Success -> {
                        result.data?.let { data ->
                            _ipQueryData.update {
                                data
                            }
                        }
                    }
                }
            }
        }
    }
}