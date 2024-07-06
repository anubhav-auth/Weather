package com.example.weather.data.model.weather

data class WeatherData(
    val alerts: Alerts,
    val current: Current,
    val forecast: Forecast,
    val location: Location
)