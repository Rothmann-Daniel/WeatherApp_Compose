package com.danielrothmann.weatherappcompose.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
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
        InfoDetailsCard()
        TablayoutDetails()
        // ListItemCard()
    }


}

@Preview
@Composable
fun InfoDetailsCard(modifier: Modifier = Modifier) {
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
                    text = "20 Jan 2026 14:08",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                AsyncImage(
                    model = "https://cdn.weatherapi.com/weather/64x64/night/116.png",
                    contentDescription = "condition",
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                text = "City",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "23°C",
                style = TextStyle(
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "Condition",
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
                    text = "23°C / 17°C",
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

@Preview
@Composable
fun TablayoutDetails(modifier: Modifier = Modifier) {
    val tabList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

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

        // HorizontalPager ЗА TabRow
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> {
                    Log.d("TAG", "HoursWeatherScreen")
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            listOf(
                                WeatherModel(
                                    "Moscow",
                                    "20 Jan 2026 17:45",
                                    "Light rain",
                                    "//cdn.weatherapi.com/weather/64x64/day/296.png",
                                    "23",
                                    "",
                                    "",
                                    ""
                                ),
                                WeatherModel(
                                    "Moscow",
                                    "20 Jan 2026 17:45",
                                    "Light rain",
                                    "//cdn.weatherapi.com/weather/64x64/day/296.png",
                                    "",
                                    "17",
                                    "11",
                                    "SomeInfo"
                                )
                            )
                        ) { _, item ->
                            ListItemCard(item)
                        }
                    }
                }

                1 -> {
                    Log.d("TAG", "DaysWeatherScreen")
                }

                else -> Log.d("TAG", "Unknown page")
            }
        }
    }
}