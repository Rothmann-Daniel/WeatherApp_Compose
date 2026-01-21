package com.danielrothmann.weatherappcompose.screens

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
fun ListItemCard(itemModel: WeatherModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground.copy(0.3f)),
        shape = RoundedCornerShape(5.dp)
    ){
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
                Text(
                    text = itemModel.localtime,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = itemModel.condition,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }
            Text(
                text = if (itemModel.currentTemp.isNotEmpty()) {
                    "${itemModel.currentTemp}°C"
                } else {
                    "${itemModel.maxTemp}°/${itemModel.minTemp}°C"
                },
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            AsyncImage(
                model = "https:${itemModel.imageUrl}",
                contentDescription = "itemModel.imageUr",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}