package com.example.weather.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.data.ApiKey
import com.example.weather.data.model.location.LocationQueryData
import com.example.weather.data.model.location.LocationQueryDataItem

@Composable
fun SearchScreen(viewModel: WeatherViewModel, navController: NavController) {
    SearchBar(viewModel, navController)
//    in navigate component when a new composabel is called check in log if going back runs composable then put viewmodel logic there also
}

@Composable
fun SearchBar(viewModel: WeatherViewModel, navController: NavController) {
    val apiKey = ApiKey.WEATHER_API_KEY
    var text by remember {
        mutableStateOf("")
    }
    val locationQueries by viewModel.locationQueryData.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = {
                viewModel.fetchLocationQueryData(apiKey, it)
                text = it
            },
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 21.dp)
                .clip(RoundedCornerShape(21.dp))
                .fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(fontSize = 21.sp)
        )
        Spacer(modifier = Modifier.size(21.dp))
        locationQueries?.let {
            LocationQueryMenu(item = it, navController = navController)
        }
    }
}

@Composable
fun LocationQueryMenu(item: LocationQueryData, navController: NavController) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 21.dp)
    ) {
        items(item.size) { index ->
            LocationQueryItem(
                item = item[index],
                navController = navController,
                modifier = Modifier
            )
            HorizontalDivider(
                thickness = 1.dp, modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .padding(vertical = 9.dp)
            )
        }
    }
}

@Composable
fun LocationQueryItem(
    modifier: Modifier = Modifier,
    item: LocationQueryDataItem,
    navController: NavController
) {
    Box(modifier = modifier
        .fillMaxSize()
        .clickable {
            navController.navigate("searched_display/${item.name}")
        }) {
        Text(text = item.name, fontSize = 24.sp, color = Color.White)

    }
}