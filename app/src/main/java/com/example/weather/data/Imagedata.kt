package com.example.weather.data

import com.example.weather.R

object Imagedata {
    fun getImg(str: String?): Int {
        return when {
            str == "Sunny" -> R.drawable.weather_sunny
            str == "Partly cloudy" -> R.drawable.weather_partly_cloudy
            str == "Cloudy" -> R.drawable.weather_cloudy
            str == "Overcast" -> R.drawable.weather_overcast
            str == "Mist" -> R.drawable.weather_mist
            str == "Patchy rain possible" -> R.drawable.weather_patchy_rain_possible
            str == "Patchy snow possible" -> R.drawable.weather_patchy_snow_possible
            str == "Patchy sleet possible" -> R.drawable.weather_patchy_sleet_possible
            str == "Patchy freezing drizzle possible" -> R.drawable.weather_patchy_rain_possible
            str == "Thundery outbreaks possible" -> R.drawable.weather_thundary_outbreaks
            str == "Blowing snow" -> R.drawable.weather_blowing_snow
            str == "Blizzard" -> R.drawable.weather_blizzard
            str == "Fog" -> R.drawable.weather_fog
            str == "Freezing fog" -> R.drawable.weather_fog
            str == "Patchy light drizzle" -> R.drawable.weather_light_rain
            str == "Light drizzle" -> R.drawable.weather_light_rain
            str == "Freezing drizzle" -> R.drawable.weather_light_rain
            str == "Heavy freezing drizzle" -> R.drawable.weather_heavy_rain
            str == "Patchy light rain" -> R.drawable.weather_patchy_rain_possible
            str == "Light rain" -> R.drawable.weather_light_rain
            str == "Moderate rain at times" -> R.drawable.weather_light_rain
            str == "Moderate rain" -> R.drawable.weather_light_rain
            str == "Heavy rain at times" -> R.drawable.weather_heavy_rain
            str == "Heavy rain" -> R.drawable.weather_heavy_rain
            str == "Light freezing rain" -> R.drawable.weather_light_rain
            str == "Moderate or heavy freezing rain" -> R.drawable.weather_heavy_rain
            str == "Light sleet" -> R.drawable.weather_patchy_sleet_possible
            str == "Moderate or heavy sleet" -> R.drawable.weather_patchy_sleet_possible
            str == "Patchy light snow" -> R.drawable.weather_patchy_snow_possible
            str == "Light snow" -> R.drawable.weather_patchy_snow_possible
            str == "Patchy moderate snow" -> R.drawable.weather_patchy_snow_possible
            str == "Moderate snow" -> R.drawable.weather_snowing
            str == "Patchy heavy snow" -> R.drawable.weather_snowing
            str == "Heavy snow" -> R.drawable.weather_blowing_snow
            str == "Ice pellets" -> R.drawable.weather_snowing
            str == "Light rain shower" -> R.drawable.weather_light_rain
            str == "Moderate or heavy rain shower" -> R.drawable.weather_heavy_rain
            str == "Torrential rain shower" -> R.drawable.weather_heavy_rain
            str == "Light sleet showers" -> R.drawable.weather_patchy_sleet_possible
            str == "Moderate or heavy sleet showers" -> R.drawable.weather_blizzard
            str == "Light snow showers" -> R.drawable.weather_blowing_snow
            str == "Moderate or heavy snow showers" -> R.drawable.weather_blowing_snow
            str == "Light showers of ice pellets" -> R.drawable.weather_patchy_sleet_possible
            str == "Moderate or heavy showers of ice pellets" -> R.drawable.weather_blizzard
            str == "Patchy light rain with thunder" -> R.drawable.weather_thunder_rain
            str == "Moderate or heavy rain with thunder" -> R.drawable.weather_thunder_rain
            str == "Patchy light snow with thunder" -> R.drawable.weather_thunder_snow
            str == "Moderate or heavy snow with thunder" -> R.drawable.weather_thunder_snow
            else -> R.drawable.weather_mist
        }
    }
}