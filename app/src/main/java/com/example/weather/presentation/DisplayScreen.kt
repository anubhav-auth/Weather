package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.R
import com.example.weather.data.ApiKey
import com.example.weather.data.ImageData
import com.example.weather.data.model.weather.Forecastday
import com.example.weather.data.model.weather.Hour
import com.example.weather.data.model.weather.WeatherData
import com.example.weather.ui.theme.cardBackgroundColor
import com.example.weather.ui.theme.weatherDayBackGrad1
import com.example.weather.ui.theme.weatherDayBackGrad2
import com.example.weather.ui.theme.weatherDayBackGrad3
import com.example.weather.ui.theme.weatherNightBackGrad1
import com.example.weather.ui.theme.weatherNightBackGrad3
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.Locale


fun String.getTimePart(): String {
    return this.split(" ")[1]
}

fun String.convertTo12Hr(): String {
    val hr = this.split(":")[0].toInt()
    return if (hr == 0) "12:00 AM"
    else if (hr == 12) "12:00 PM"
    else if (hr > 12) "${hr - 12}:00 PM"
    else "${hr}:00 AM"
}

fun Double.decimalPlace2(): Double {
    return String.format(Locale("English"), "%.2f", this).toDouble()
}

fun generateHourlyForecastList(data: Forecastday?): List<ForecastHourlyContent> {
    val forecastList = mutableListOf<ForecastHourlyContent>()

    val hours: List<Hour>? = data?.hour
    for (i in 0..23) {
        hours?.get(i)?.let {
            forecastList.add(
                ForecastHourlyContent(
                    it.time,
                    it.condition.text,
                    it.temp_c.toString(),
                    it.is_day
                )
            )
        }
    }
    return forecastList
}

@Composable
fun DisplayScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient,
    location: String
) {
    val apiKey = ApiKey.WEATHER_API_KEY

    viewModel.fetchWeatherData(apiKey, location)
    val weatherData by viewModel.weatherData.collectAsState()
    if (weatherData == null) {
        SplashScreen()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            weatherData?.let { weatherData ->
                Column(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            navController.navigate("search_screen")
                                        },
                                    tint = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "reload",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            viewModel.getLatLang(fusedLocationProviderClient = fusedLocationProviderClient)
                                            viewModel.fetchWeatherData(
                                                apiKey,
                                                location,
                                                8,
                                                "yes",
                                                "no"
                                            )
                                        },
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.size(30.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 21.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(Modifier.fillMaxWidth(0.55f)) {
                                    Text(
                                        text = "${weatherData.current.temp_c}°C",
                                        color = Color.White,
                                        fontSize = 51.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = weatherData.current.condition.text,
                                        color = Color.White,
                                        softWrap = true,
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = weatherData.location.name, color = Color.White,
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = "location",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = "${weatherData.forecast.forecastday[0].day.maxtemp_c}°/${
                                            weatherData.forecast.forecastday[0].day.mintemp_c
                                        }°",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Light,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                }
                                ImageData.getImg(weatherData.current.condition.text)
                                    ?.let { imageData ->
                                        Image(
                                            painter = painterResource(id = imageData),
                                            contentDescription = weatherData.current.condition.text,
                                            modifier = Modifier.fillMaxSize(),//.size(120.dp),
                                            alignment = Alignment.Center
                                        )
                                    }

                            }


                            val hourlyForecastList =
                                generateHourlyForecastList(weatherData.forecast.forecastday[0])

                            Spacer(modifier = Modifier.size(30.dp))
                            Text(
                                text = "${weatherData.forecast.forecastday[0].day.condition.text}. Low ${weatherData.forecast.forecastday[0].day.mintemp_c}°C",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraLight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 21.dp),
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            ForecastHourlyMenu(items = hourlyForecastList)

                            Box(
                                modifier = Modifier
                                    .padding(21.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .fillMaxWidth()
                                    .height(45.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(
                                                weatherNightBackGrad1,
                                                weatherDayBackGrad3,
                                                weatherDayBackGrad2
                                            )
                                        )
                                    )
                                    .clickable {
                                        navController.navigate("future_prediction_screen")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "3-DAY FORECAST",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }



                            WeatherDetailItem(
                                item = WeatherDetailContent(
                                    weatherData,
                                    weatherData.location.name,
                                    weatherData.location.region,
                                    weatherData.forecast.forecastday[0].astro.sunrise,
                                    weatherData.forecast.forecastday[0].astro.sunset,
                                    weatherData.forecast.forecastday[0].astro.moonrise,
                                    weatherData.forecast.forecastday[0].astro.moonset,
                                    weatherData.current.feelslike_c,
                                    weatherData.forecast.forecastday[0].day.maxtemp_c,
                                    weatherData.forecast.forecastday[0].day.mintemp_c,
                                    weatherData.current.wind_kph,
                                    weatherData.current.wind_degree,
                                    weatherData.current.wind_dir,
                                    weatherData.current.humidity,
                                    weatherData.current.dewpoint_c,
                                    weatherData.current.pressure_mb,
                                    weatherData.current.uv,
                                    weatherData.current.vis_km,
                                    weatherData.forecast.forecastday[0].astro.moon_phase
                                )
                            )
                            AirQualityItem(
                                item = AirQualityContent(
                                    weatherData.forecast.forecastday[0].day.air_quality.co,
                                    weatherData.forecast.forecastday[0].day.air_quality.no2,
                                    weatherData.forecast.forecastday[0].day.air_quality.o3,
                                    weatherData.forecast.forecastday[0].day.air_quality.so2,
                                    weatherData.forecast.forecastday[0].day.air_quality.pm2_5,
                                    weatherData.forecast.forecastday[0].day.air_quality.pm10,
                                    weatherData.forecast.forecastday[0].day.air_quality.usEpaIndex,
                                    weatherData.forecast.forecastday[0].day.air_quality.gbDefraIndex,
                                )
                            )
                        }

                    }
                }
            }
        }
    }

}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.weather_splash),
            contentDescription = "",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Composable
fun ForecastHourlyMenu(items: List<ForecastHourlyContent>) {
    LazyRow {
        items(items) { item ->
            ForecastHourlyElement(item = item)
        }
    }
}

@Composable
fun ForecastHourlyElement(item: ForecastHourlyContent) {

    val backGroundColor = if (item.isDay == 0) listOf(
        weatherNightBackGrad1,
        weatherDayBackGrad3, weatherNightBackGrad3
    ) else
        listOf(weatherDayBackGrad1, weatherDayBackGrad2, weatherDayBackGrad3)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(height = 84.dp, width = 72.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = Brush.linearGradient(backGroundColor))
    ) {
        Text(
            text = "${item.time?.getTimePart()?.convertTo12Hr()}",
            color = Color.White,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.size(3.dp))
        ImageData.getImg(item.icon)?.let { imageData ->
            Image(
                painter = painterResource(id = imageData),
                contentDescription = item.icon,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.size(3.dp))
        Text(text = "${item.temp}°", color = Color.White)
    }
}

data class ForecastHourlyContent(
    val time: String?,
    val icon: String?,
    val temp: String?,
    val isDay: Int
)

@Composable
fun WeatherDetailItem(item: WeatherDetailContent) {
    Column(
        modifier = Modifier
            .padding(21.dp)
            .clip(
                RoundedCornerShape(15.dp),
            )
            .background(cardBackgroundColor)
            .padding(12.dp)
    ) {
        Text(
            text = "Weather Today in ${item.city}, ${item.region}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.size(21.dp))

        Text(
            text = "Feels Like", color = Color.White, fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "${item.feelsLike}°", color = Color.White, fontSize = 33.sp,
            fontWeight = FontWeight.ExtraBold
        )

//        sun times
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_sunrise),
                    contentDescription = "",
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = item.sunrise, color = Color.White)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_sunset),
                    contentDescription = "",
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = item.sunset, color = Color.White)
            }
        }

//        moon times
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_moon_rise),
                    contentDescription = "",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = item.moonrise, color = Color.White)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_moon_set),
                    contentDescription = "",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = item.moonSet, color = Color.White)
            }
        }


//        high/low
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_high_low),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "High/Low", color = Color.White)
            }
            Text(
                text = "${item.weatherData.forecast.forecastday[0].day.maxtemp_c}°/${item.weatherData.forecast.forecastday[0].day.mintemp_c}°",
                color = Color.White
            )
        }

//        wind
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_wind),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Wind", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.wind}km/h",
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "${item.windDegree}° ${item.windDirection}",
                    color = Color.White
                )
            }

        }

//        humidity
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_humidity),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Humidity", color = Color.White)
            }
            Text(
                text = "${item.humidity}%",
                color = Color.White
            )
        }

//        dew Point
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_dew_point),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Dew Point", color = Color.White)
            }
            Text(
                text = "${item.dewPoint}°",
                color = Color.White
            )
        }

//        Pressure
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_pressure),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Pressure", color = Color.White)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "${item.pressure} mb",
                    color = Color.White
                )
            }
        }

//        uv index
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_uv_index),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "UV Index", color = Color.White)
            }
            Text(
                text = "${item.uvIndex.toInt()} of 11",
                color = Color.White
            )
        }

//        visibility
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_visibility),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Visibility", color = Color.White)
            }
            Text(
                text = "${item.visibility} km",
                color = Color.White
            )
        }

//        moon phase
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_moon_phases),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Moon Phase", color = Color.White)
            }
            Text(
                text = item.moonPhase,
                color = Color.White
            )
        }

    }
}

data class WeatherDetailContent(
    val weatherData: WeatherData,
    val city: String,
    val region: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonSet: String,
    val feelsLike: Double,
    val tempHigh: Double,
    val tempLow: Double,
    val wind: Double,
    val windDegree: Double,
    val windDirection: String,
    val humidity: Int,
    val dewPoint: Double,
    val pressure: Double,
    val uvIndex: Double,
    val visibility: Double,
    val moonPhase: String
)


@Composable
fun AirQualityItem(item: AirQualityContent) {
    Column(
        modifier = Modifier
            .padding(21.dp)
            .clip(
                RoundedCornerShape(15.dp),
            )
            .background(cardBackgroundColor)
            .padding(12.dp)
    ) {
        Text(
            text = "Air Quality Today",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.size(21.dp))

//        co
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Carbon Monoxide", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.co.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }

//        no2
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nitrogen Dioxide", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.no2.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }

//        o3
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ozone", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.o3.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }

//        so2
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sulphur Dioxide", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.so2.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }

//        pm2.5
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PM 2.5", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.pm25.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }

//        pm10
        HorizontalDivider(
            thickness = 1.dp, modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 9.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PM 10", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${item.pm10.decimalPlace2()} µg/m³",
                    color = Color.White
                )
            }

        }
    }
}

data class AirQualityContent(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm25: Double,
    val pm10: Double,
    val usEpaIndex: Int,
    val gbDefrayIndex: Int
)

