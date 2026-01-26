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
            .padding(vertical = 4.dp, horizontal = 8.dp)
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
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
            ) {
                // Определяем что показывать: дату или время
                val timeToShow = when {
                    showTimeInsteadOfDate && item.time.contains(" ") ->
                        item.time.substringAfter(" ") // "HH:mm" для часового прогноза
                    item.localtime.isNotEmpty() ->
                        item.localtime // Время обновления для текущего дня
                    else ->
                        item.time // Дата для прогноза на дни
                }

                Text(
                    text = timeToShow,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = item.condition,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }

            // Безопасное отображение температуры
            val tempText = try {
                if (item.currentTemp.isNotEmpty()) {
                    "${item.currentTemp.toFloatOrNull()?.toInt() ?: 0}°C"
                } else {
                    "${item.maxTemp.toFloatOrNull()?.toInt() ?: "N/A"}°/${item.minTemp.toFloatOrNull()?.toInt() ?: "N/A"}°C"
                }
            } catch (e: Exception) {
                "N/A"
            }

            Text(
                text = tempText,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            AsyncImage(
                model = "https:${item.imageUrl}",
                contentDescription = "weather icon",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}