package com.danielrothmann.weatherappcompose.data

data class WeatherModel(
    val city: String,
    val localtime: String,
    val condition: String,
    val imageUrl: String,
    val currentTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String
)
