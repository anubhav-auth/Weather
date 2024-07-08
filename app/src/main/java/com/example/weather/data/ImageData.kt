package com.example.weather.data

import com.example.weather.R


object ImageData {
    fun getImg(str: String?): Int? {
        return when (str) {
            "Sunny" -> R.drawable.weather_sunny
            "Clear " -> R.drawable.weather_clear
            "Partly Cloudy " -> R.drawable.weather_partly_cloudy
            "Partly Cloudy" -> R.drawable.weather_partly_cloudy
            "Cloudy " -> R.drawable.weather_cloudy
            "Overcast " -> R.drawable.weather_overcast
            "Mist" -> R.drawable.weather_mist
            "Patchy rain nearby" -> R.drawable.weather_patchy_rain_possible
            "Patchy snow nearby" -> R.drawable.weather_patchy_snow_possible
            "Patchy sleet nearby" -> R.drawable.weather_patchy_sleet_possible
            "Patchy freezing drizzle nearby" -> R.drawable.weather_patchy_rain_possible
            "Thundery outbreaks in nearby" -> R.drawable.weather_thundary_outbreaks
            "Blowing snow" -> R.drawable.weather_blowing_snow
            "Blizzard" -> R.drawable.weather_blizzard
            "Fog" -> R.drawable.weather_fog
            "Freezing fog" -> R.drawable.weather_fog
            "Patchy light drizzle" -> R.drawable.weather_light_rain
            "Light drizzle" -> R.drawable.weather_light_rain
            "Freezing drizzle" -> R.drawable.weather_light_rain
            "Heavy freezing drizzle" -> R.drawable.weather_heavy_rain
            "Patchy light rain" -> R.drawable.weather_patchy_rain_possible
            "Light rain" -> R.drawable.weather_light_rain
            "Moderate rain at times" -> R.drawable.weather_light_rain
            "Moderate rain" -> R.drawable.weather_light_rain
            "Heavy rain at times" -> R.drawable.weather_heavy_rain
            "Heavy rain" -> R.drawable.weather_heavy_rain
            "Light freezing rain" -> R.drawable.weather_light_rain
            "Moderate or heavy freezing rain" -> R.drawable.weather_heavy_rain
            "Light sleet" -> R.drawable.weather_patchy_sleet_possible
            "Moderate or heavy sleet" -> R.drawable.weather_patchy_sleet_possible
            "Patchy light snow" -> R.drawable.weather_patchy_snow_possible
            "Light snow" -> R.drawable.weather_patchy_snow_possible
            "Patchy moderate snow" -> R.drawable.weather_patchy_snow_possible
            "Moderate snow" -> R.drawable.weather_snowing
            "Patchy heavy snow" -> R.drawable.weather_snowing
            "Heavy snow" -> R.drawable.weather_blowing_snow
            "Ice pellets" -> R.drawable.weather_snowing
            "Light rain shower" -> R.drawable.weather_light_rain
            "Moderate or heavy rain shower" -> R.drawable.weather_heavy_rain
            "Torrential rain shower" -> R.drawable.weather_heavy_rain
            "Light sleet showers" -> R.drawable.weather_patchy_sleet_possible
            "Moderate or heavy sleet showers" -> R.drawable.weather_blizzard
            "Light snow showers" -> R.drawable.weather_blowing_snow
            "Moderate or heavy snow showers" -> R.drawable.weather_blowing_snow
            "Light showers of ice pellets" -> R.drawable.weather_patchy_sleet_possible
            "Moderate or heavy showers of ice pellets" -> R.drawable.weather_blizzard
            "Patchy light rain in area with thunder" -> R.drawable.weather_thunder_rain
            "Moderate or heavy rain with thunder" -> R.drawable.weather_thunder_rain
            "Patchy light snow with thunder" -> R.drawable.weather_thunder_snow
            "Moderate or heavy snow with thunder" -> R.drawable.weather_thunder_snow
            else -> null
        }
    }
}