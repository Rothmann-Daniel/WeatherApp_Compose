package com.danielrothmann.weatherappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.danielrothmann.weatherappcompose.screens.MainScreen
import com.danielrothmann.weatherappcompose.ui.theme.WeatherAppComposeTheme

class MainActivity : ComponentActivity() {

    companion object {
        // Константы
        const val WEATHER_API_KEY = "b8c98a14f49644988ae92245252006"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize().statusBarsPadding()) { innerPadding ->
                   MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}




