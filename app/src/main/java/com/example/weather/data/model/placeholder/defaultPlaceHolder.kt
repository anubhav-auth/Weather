package com.example.weather.data.model.placeholder

import com.example.weather.data.model.weather.Alerts
import com.example.weather.data.model.weather.Current
import com.example.weather.data.model.weather.Forecast
import com.example.weather.data.model.weather.Location

data class LocationPlaceholder(
    val name: String = "London",
    val region: String = "City of London, Greater London",
    val country: String = "United Kingdom",
    val lat: Double = 51.52,
    val lon: Double = -0.11,
    val tz_id: String = "Europe/London",
    val localtime_epoch: Long = 1720361028,
    val localtime: String = "2024-07-07 15:03"
)
data class CurrentPlaceholder(
    val last_updated_epoch: Long = 1720360800,
    val last_updated: String = "2024-07-07 15:00",
    val temp_c: Double = 13.2,
    val temp_f: Double = 55.8,
    val is_day: Int = 1,
    val condition: ConditionPlaceholder = ConditionPlaceholder(),
    val wind_mph: Double = 12.5,
    val wind_kph: Double = 20.2,
    val wind_degree: Int = 220,
    val wind_dir: String = "SW",
    val pressure_mb: Double = 1010.0,
    val pressure_in: Double = 29.83,
    val precip_mm: Double = 0.39,
    val precip_in: Double = 0.02,
    val humidity: Int = 88,
    val cloud: Int = 50,
    val feelslike_c: Double = 11.7,
    val feelslike_f: Double = 53.1,
    val windchill_c: Double = 17.0,
    val windchill_f: Double = 62.7,
    val heatindex_c: Double = 17.0,
    val heatindex_f: Double = 62.7,
    val dewpoint_c: Double = 11.0,
    val dewpoint_f: Double = 51.8,
    val vis_km: Double = 10.0,
    val vis_miles: Double = 6.0,
    val uv: Int = 4,
    val gust_mph: Double = 14.1,
    val gust_kph: Double = 22.8,
    val air_quality: AirQualityPlaceholder = AirQualityPlaceholder()
)

data class ConditionPlaceholder(
    val text: String = "Light rain",
    val icon: String = "//cdn.weatherapi.com/weather/64x64/day/296.png",
    val code: Int = 1183
)

data class AirQualityPlaceholder(
    val co: Double = 156.9,
    val no2: Double = 2.6,
    val o3: Double = 68.7,
    val so2: Double = 1.8,
    val pm2_5: Double = 0.5,
    val pm10: Double = 0.6,
    val us_epa_index: Int = 1,
    val gb_defra_index: Int = 1
)
data class ForecastPlaceholder(
    val forecastday: List<ForecastDayPlaceholder> = listOf()
)

data class ForecastDayPlaceholder(
    val date: String = "2024-07-07",
    val date_epoch: Long = 1720310400,
    val day: DayPlaceholder = DayPlaceholder(),
    val astro: AstroPlaceholder = AstroPlaceholder(),
    val hour: List<HourPlaceholder> = listOf()
)

data class DayPlaceholder(
    val maxtemp_c: Double = 17.1,
    val maxtemp_f: Double = 62.7,
    val mintemp_c: Double = 10.2,
    val mintemp_f: Double = 50.4,
    val avgtemp_c: Double = 13.8,
    val avgtemp_f: Double = 56.9,
    val maxwind_mph: Double = 11.4,
    val maxwind_kph: Double = 18.4,
    val totalprecip_mm: Double = 5.25,
    val totalprecip_in: Double = 0.21,
    val totalsnow_cm: Double = 0.0,
    val avgvis_km: Double = 8.8,
    val avgvis_miles: Double = 5.0,
    val avghumidity: Int = 79,
    val daily_will_it_rain: Int = 1,
    val daily_chance_of_rain: Int = 96,
    val daily_will_it_snow: Int = 0,
    val daily_chance_of_snow: Int = 0,
    val condition: ConditionPlaceholder = ConditionPlaceholder(),
    val uv: Int = 5,
    val air_quality: AirQualityPlaceholder = AirQualityPlaceholder()
)
data class AstroPlaceholder(
    val sunrise: String = "04:53 AM",
    val sunset: String = "09:18 PM",
    val moonrise: String = "05:53 AM",
    val moonset: String = "10:49 PM",
    val moon_phase: String = "Waxing Crescent",
    val moon_illumination: Int = 1,
    val is_moon_up: Int = 0,
    val is_sun_up: Int = 0
)
data class HourPlaceholder(
    val time_epoch: Long = 1720306800,
    val time: String = "2024-07-07 00:00",
    val temp_c: Double = 10.9,
    val temp_f: Double = 51.6,
    val is_day: Int = 0,
    val condition: ConditionPlaceholder = ConditionPlaceholder(),
    val wind_mph: Double = 5.6,
    val wind_kph: Double = 9.0,
    val wind_degree: Int = 220,
    val wind_dir: String = "SW",
    val pressure_mb: Double = 1009.0,
    val pressure_in: Double = 29.79,
    val precip_mm: Double = 0.0,
    val precip_in: Double = 0.0,
    val snow_cm: Double = 0.0,
    val humidity: Int = 81,
    val cloud: Int = 11,
    val feelslike_c: Double = 9.9,
    val feelslike_f: Double = 49.7,
    val windchill_c: Double = 9.9,
    val windchill_f: Double = 49.7,
    val heatindex_c: Double = 10.9,
    val heatindex_f: Double = 51.6,
    val dewpoint_c: Double = 7.8,
    val dewpoint_f: Double = 46.0,
    val will_it_rain: Int = 0,
    val chance_of_rain: Int = 0,
    val will_it_snow: Int = 0,
    val chance_of_snow: Int = 0,
    val vis_km: Double = 10.0,
    val vis_miles: Double = 6.0,
    val gust_mph: Double = 9.9,
    val gust_kph: Double = 16.0,
    val uv: Int = 1,
    val air_quality: AirQualityPlaceholder = AirQualityPlaceholder()
)
