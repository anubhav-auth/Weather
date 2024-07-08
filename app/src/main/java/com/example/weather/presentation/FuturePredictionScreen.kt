package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.data.ApiKey
import com.example.weather.data.ImageData
import com.example.weather.data.model.weather.Forecast
import com.example.weather.data.model.weather.Forecastday
import com.example.weather.ui.theme.cardBackgroundColor
import kotlin.math.sign

fun String.dateFormat(): String {
    val a = this.split("-")
    return "${a[2]}/${a[1]}/${a[0]}"
}

@Composable
fun FuturePredictionScreen(viewModel: WeatherViewModel, location: String) {


    val weatherData by viewModel.weatherData.collectAsState()
    weatherData?.let {
        ForecastMenu(items = it.forecast)
    }
}

@Composable
fun ForecastMenu(items: Forecast) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(items.forecastday) { data ->
            ForecastItem(item = data)
        }
    }
}

@Composable
fun ForecastItem(item: Forecastday) {
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
            text = item.date.dateFormat(),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.size(21.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_humidity),
                    contentDescription = "humidity",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "${item.day.avghumidity}%", color = Color.White, fontSize = 15.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weather_precipitation),
                    contentDescription = "precipitation",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "${item.day.daily_chance_of_rain}%", color = Color.White, fontSize = 15.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ImageData.getImg(item.day.condition.text)?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "${item.day.maxtemp_c}°/${item.day.mintemp_c}°", color = Color.White, fontSize = 15.sp)
            }


        }

    }
}