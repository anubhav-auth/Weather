package com.example.weather.data

import com.example.weather.data.model.ipQuery.IPQuery
import com.example.weather.data.model.location.LocationQueryData
import com.example.weather.data.model.weather.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): WeatherData

    @GET("search.json")
    suspend fun locationQuery(
        @Query("key") key: String,
        @Query("q") q: String
    ): LocationQueryData

    @GET("ip.json")
    suspend fun ipQuery(
        @Query("q") q: String,
        @Query("key") key: String
    ): IPQuery

    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

}
