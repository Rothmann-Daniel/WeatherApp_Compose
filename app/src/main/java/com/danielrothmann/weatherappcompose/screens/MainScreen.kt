package com.danielrothmann.weatherappcompose.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielrothmann.weatherappcompose.R
import com.danielrothmann.weatherappcompose.data.WeatherModel
import com.danielrothmann.weatherappcompose.ui.theme.CardBackground
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>
) {
    Image(
        painter = painterResource(id = R.drawable.weather_background),
        contentDescription = "weather_background",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f),
        contentScale = ContentScale.Crop // Использую Crop вместо FillBounds (по умолчанию)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InfoDetailsCard(currentDay = currentDay)
        TablayoutDetails(daysList = daysList, currentDay = currentDay)
    }


}

@Preview
@Composable
fun InfoDetailsCard(
    modifier: Modifier = Modifier,
    currentDay: MutableState<WeatherModel> = mutableStateOf(
        WeatherModel(
            "", "", "", "", "", "", "", "",""
        )
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground.copy(0.3f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentDay.value.localtime,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                AsyncImage(
                    model = "https:" + currentDay.value.imageUrl,
                    contentDescription = "condition",
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                text = currentDay.value.city,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            // Безопасное преобразование температуры
            val tempText = try {
                "${currentDay.value.currentTemp.toFloatOrNull()?.toInt() ?: 0}°C"
            } catch (e: Exception) {
                "N/A"
            }
            Text(
                text = tempText,
                style = TextStyle(
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = currentDay.value.condition,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { /*TODO*/
                        Log.d("TAG", "Click btn_search ")
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_searh),
                        contentDescription = "search",
                        tint = Color.White
                    )
                }
                Text(
                    text = "max:" +
                            "${
                                currentDay.value.maxTemp.toFloatOrNull()?.toInt() ?: "N/A"
                            }°C" + " / " +
                            "min: "
                            + "${currentDay.value.minTemp.toFloatOrNull()?.toInt() ?: "N/A"}°C",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                )
                IconButton(
                    onClick = { /*TODO*/
                        Log.d("TAG", "Click btn_sync ")
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.btm_sync),
                        contentDescription = "sync",
                        tint = Color.White
                    )
                }

            }
        }
    }
}

@Composable
fun TablayoutDetails(
    modifier: Modifier = Modifier,
    daysList: MutableState<List<WeatherModel>> = mutableStateOf(listOf()),
    currentDay: MutableState<WeatherModel>
) {
    val tabList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    // Вычисляем список для отображения
    val displayList = remember(tabIndex, currentDay.value.hours, daysList.value) {
        when (tabIndex) {
            0 -> getWeatherByHours(currentDay.value.hours) // Часы
            1 -> daysList.value                            // Дни
            else -> daysList.value
        }
    }

    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = CardBackground.copy(alpha = 0.5f),
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    height = 2.dp,
                    color = Color.White
                )
            }
        ) {
            tabList.forEachIndexed { index, textTitle ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = textTitle,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = if (tabIndex == index) Color.White else Color.White.copy(
                                    alpha = 0.7f
                                ),
                                fontWeight = if (tabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val showTimeInsteadOfDate = page == 0 // true для часов
            val isClickable = page == 1 // Кликабельны только дни

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(displayList) { index, item ->
                    ListItemCard(
                        item = item,
                        currentDay = currentDay,
                        showTimeInsteadOfDate = showTimeInsteadOfDate,
                        isClickable = isClickable
                    )
                }
            }
        }
    }
}


private fun getWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()

    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                city = "",
                time = item.getString("time"), // "YYYY-MM-DD HH:mm"
                condition = item.getJSONObject("condition").getString("text"),
                imageUrl = item.getJSONObject("condition").getString("icon"),
                currentTemp = item.getString("temp_c"),
                maxTemp = "",
                minTemp = "",
                hours = "",
                localtime = ""
            )
        )
    }
    return list
}