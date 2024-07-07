package com.example.weather.Presentation

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.R
import com.example.weather.data.ApiKey
import com.example.weather.data.Imagedata
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

fun String.toTitleCase(): String {
    return "${this[0].uppercase()}${this.substring(1)}"
}

fun String.getTimePart(): String {
    return this.split(" ")[1]
}

fun String.convertTo12Hr(): String {
    val hr = this.split(":")[0].toInt()
    if (hr == 0) return "12:00 AM"
    else if (hr == 12) return "12:00 PM"
    else if (hr > 12) return "${hr - 12}:00 PM"
    else return "${hr}:00 AM"
}
fun Double.DecimalPlace2():Double{
    return String.format("%.2f", this).toDouble()
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
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    val context = LocalContext.current
    val apiKey = ApiKey.WEATHER_API_KEY

    val a = viewModel.getLatLang(fusedLocationProviderClient = fusedLocationProviderClient)
    Log.d("my tag", a)

    viewModel.fetchWeatherData(apiKey, a, 8, "yes", "no")

    val weatherData = viewModel.weatherData.collectAsState().value



    LaunchedEffect(key1 = viewModel.showErrorChannel) {
//        viewModel.showErrorChannel.collectLatest { show ->
//            if (show) Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
//        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        weatherData?.let {
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
                                .padding(end = 12.dp, top = 12.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search",
                                modifier = Modifier.size(36.dp).clickable {
                                    navController.navigate("search_screen")
                                },
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.size(50.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 21.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    text = "${it.current.temp_c}°C",
                                    color = Color.White,
                                    fontSize = 51.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(
                                    text = it.current.condition.text, color = Color.White,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.size(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.location.name, color = Color.White,
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

                                Spacer(modifier = Modifier.size(6.dp))
                            }
                            Image(
                                painter = painterResource(id = Imagedata.getImg(it.current.condition.text)),
                                contentDescription = it.current.condition.text,
                                modifier = Modifier.size(120.dp)
                            )
                        }
                        Text(
                            text = "${it.forecast.forecastday[0].day.maxtemp_c}°/${
                                it.forecast.forecastday[0].day.mintemp_c
                            }°",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 21.dp),
                            textAlign = TextAlign.Start
                        )

                        val hourlyForecastList =
                            generateHourlyForecastList(it.forecast.forecastday[0])

                        Spacer(modifier = Modifier.size(30.dp))
                        Text(
                            text = "${it.forecast.forecastday[0].day.condition.text}. Low ${it.forecast.forecastday[0].day.mintemp_c}°C",
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

                        Spacer(modifier = Modifier.size(15.dp))
                        WeatherDetailItem(
                            item = WeatherDetailContent(
                                it,
                                it.location.name,
                                it.location.region,
                                it.forecast.forecastday[0].astro.sunrise,
                                it.forecast.forecastday[0].astro.sunset,
                                it.forecast.forecastday[0].astro.moonrise,
                                it.forecast.forecastday[0].astro.moonset,
                                it.current.feelslike_c,
                                it.forecast.forecastday[0].day.maxtemp_c,
                                it.forecast.forecastday[0].day.mintemp_c,
                                it.current.wind_kph,
                                it.current.wind_degree,
                                it.current.wind_dir,
                                it.current.humidity,
                                it.current.dewpoint_c,
                                it.current.pressure_mb,
                                it.current.uv,
                                it.current.vis_km,
                                it.forecast.forecastday[0].astro.moon_phase
                            )
                        )
                        AirQualityItem(
                            item = AirQualityContent(
                            it.forecast.forecastday[0].day.air_quality.co,
                            it.forecast.forecastday[0].day.air_quality.no2,
                            it.forecast.forecastday[0].day.air_quality.o3,
                            it.forecast.forecastday[0].day.air_quality.so2,
                            it.forecast.forecastday[0].day.air_quality.pm2_5,
                            it.forecast.forecastday[0].day.air_quality.pm10,
                            it.forecast.forecastday[0].day.air_quality.usEpaIndex,
                            it.forecast.forecastday[0].day.air_quality.gbDefraIndex,
                            )
                        )
                    }

                }
            }
        }
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
        Image(
            painter = painterResource(id = Imagedata.getImg(item.icon)),
            contentDescription = item.icon,
            modifier = Modifier.size(20.dp)
        )
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
                text = "${item.uvIndex}} of 11",
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
                    text = "${item.co.DecimalPlace2()} µg/m³",
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
                    text = "${item.no2.DecimalPlace2()} µg/m³",
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
                    text = "${item.o3.DecimalPlace2()} µg/m³",
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
                    text = "${item.so2.DecimalPlace2()} µg/m³",
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
                    text = "${item.pm25.DecimalPlace2()} µg/m³",
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
                    text = "${item.pm10.DecimalPlace2()} µg/m³",
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
    val gbDefraIndex: Int
)