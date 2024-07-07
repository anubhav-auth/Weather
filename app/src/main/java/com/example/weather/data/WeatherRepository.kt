package com.example.weather.data

import com.example.weather.data.model.ipQuery.IPQuery
import com.example.weather.data.model.location.LocationQueryData
import com.example.weather.data.model.weather.WeatherData
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {
    suspend fun getWeatherData(
        key: String,
        q: String,
        days: Int,
        aqi: String,
        alerts: String
    ): Flow<Response<WeatherData>>

    suspend fun locationQuery(
        key: String,
        q: String
    ): Flow<Response<LocationQueryData>>

    suspend fun ipQuery(
        q: String,
        key: String
    ): Flow<Response<IPQuery>>
}