package com.danielrothmann.weatherappcompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielrothmann.weatherappcompose.data.WeatherModel
import com.danielrothmann.weatherappcompose.ui.theme.CardBackground

@Composable
fun ListItemCard(
    item: WeatherModel,
    currentDay: MutableState<WeatherModel>,
    showTimeInsteadOfDate: Boolean = false,
    isClickable: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(enabled = isClickable && item.hours.isNotEmpty()) {
                if (item.hours.isNotEmpty()) {
                    currentDay.value = item.copy(
                        currentTemp = if (item.currentTemp.isEmpty()) item.maxTemp else item.currentTemp,
                        localtime = item.time
                    )
                }
            },
        colors = CardDefaults.cardColors(containerColor = CardBackground.copy(0.3f)),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть - время и условие (40% ширины)
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Center
            ) {
                val timeToShow = when {
                    showTimeInsteadOfDate && item.time.contains(" ") ->
                        item.time.substringAfter(" ")
                    item.localtime.isNotEmpty() ->
                        item.localtime
                    else ->
                        item.time
                }

                Text(
                    text = timeToShow,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1
                )
                Text(
                    text = item.condition,
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Центр - температура (фиксированная ширина)
            val tempText = try {
                if (item.currentTemp.isNotEmpty()) {
                    "${item.currentTemp.toFloatOrNull()?.toInt() ?: 0}°C"
                } else {
                    val max = item.maxTemp.toFloatOrNull()?.toInt() ?: "N/A"
                    val min = item.minTemp.toFloatOrNull()?.toInt() ?: "N/A"
                    "$max°/$min°"
                }
            } catch (e: Exception) {
                "N/A"
            }

            Text(
                text = tempText,
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(0.3f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

            // Правая часть - иконка (30% ширины)
            AsyncImage(
                model = "https:${item.imageUrl}",
                contentDescription = "weather icon",
                modifier = Modifier
                    .weight(0.3f)
                    .size(36.dp),
                alignment = Alignment.CenterEnd
            )
        }
    }
}