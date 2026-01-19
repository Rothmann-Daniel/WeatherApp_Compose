package com.danielrothmann.weatherappcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.danielrothmann.weatherappcompose.ui.theme.WeatherAppComposeTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    companion object {
        // Константы
        const val PERMISSION_REQUEST_CODE = 1001
        const val WEATHER_API_KEY = "b8c98a14f49644988ae92245252006"

        // Статические методы
        fun isNetworkAvailable(context: Context): Boolean {
            // проверка сети
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherInfoCard(
                        name = "London",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherInfoCard(name: String, modifier: Modifier = Modifier) {
    val state = remember {
        mutableStateOf("Unknown")
    }
    val context = LocalContext.current

    // Разделяем температуру и описание
    val temperature = remember(state.value) {
        state.value.substringBefore(",").trim()
    }
    val description = remember(state.value) {
        if (state.value.contains(",")) {
            state.value.substringAfter(",").trim()
        } else {
            ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Weather in $name",
                    fontSize = 20.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Температура
                Text(
                    text = temperature,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )

                // Описание (если есть)
                if (description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontSize = 24.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            onClick = {
                getResult(name, state, context)
            }
        ) {
            Text("Refresh Weather")
        }
    }
}

private fun getResult(cityName: String, state: MutableState<String>, context: Context) {
    val url = "https://api.weatherapi.com/v1/current.json?" +
            "key=${MainActivity.WEATHER_API_KEY}" +
            "&q=$cityName" +
            "&aqi=no"

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            try {
                val jsonObject = JSONObject(response)
                val current = jsonObject.getJSONObject("current")
                val tempC = current.getDouble("temp_c")
                val condition = current.getJSONObject("condition").getString("text")

                state.value = "$tempC°C, $condition"
            } catch (e: Exception) {
                state.value = "Error: ${e.message}"
            }
        },
        { error ->
            state.value = "Error: ${error.message}"
        }
    )
    queue.add(stringRequest)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppComposeTheme {
        WeatherInfoCard("London")
    }
}


