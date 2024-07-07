package com.example.weather.data.model.weather

import com.squareup.moshi.Json

data class AirQuality(
    val co: Double,
    @Json(name = "gb-defra-index") val gbDefraIndex: Int,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double,
    @Json(name = "us-epa-index")val usEpaIndex: Int
)