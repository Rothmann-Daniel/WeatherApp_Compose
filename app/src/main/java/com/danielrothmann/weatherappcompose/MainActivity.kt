package com.danielrothmann.weatherappcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.danielrothmann.weatherappcompose.data.WeatherModel
import com.danielrothmann.weatherappcompose.screens.MainScreen
import com.danielrothmann.weatherappcompose.ui.theme.WeatherAppComposeTheme
import org.json.JSONObject

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
                val daysList = remember{
                    mutableStateOf(listOf<WeatherModel>())
                }

                val currentDay = remember{
                    mutableStateOf(WeatherModel(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    ))

                }
                // функция для получения данных
                LaunchedEffect(Unit) {
                    getResult("Moscow", daysList, currentDay, this@MainActivity)
                }

                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        daysList = daysList,
                        currentDay = currentDay
                    )
                }
            }
        }
    }
}


private fun getResult(cityName: String, daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>, context: Context) {
    val url = "https://api.weatherapi.com/v1/forecast.json?" +  // Изменено с current.json на forecast.json
            "key=${MainActivity.WEATHER_API_KEY}" +
            "&q=$cityName" +
            "&days=3" +
            "&aqi=no" +
            "&alerts=no"


    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            try {
                val list =  getWheatherByDays(response)
                currentDay.value = list[0]
                daysList.value = list


            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        },
        { error  ->
            Log.e("Error", error.message.toString())
        }
    )
    queue.add(stringRequest)
}

private fun getWheatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf()
    val mainObject = JSONObject(response)
    val list = ArrayList<WeatherModel>()
    val city = mainObject.getJSONObject("location").getString("name")
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city = city,
                time = item.getString("date"),
                localtime = "",
                condition = item.getJSONObject("day").getJSONObject("condition").getString("text"),
                imageUrl = item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                currentTemp = "",
                maxTemp = item.getJSONObject("day").getString("maxtemp_c"),
                minTemp = item.getJSONObject("day").getString("mintemp_c"),
                hours = item.getJSONArray("hour").toString()
            )
        )
    }
    // Обновляем первый элемент (текущий день)
    if (list.isNotEmpty()) {
        list[0] = list[0].copy(
            currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
            localtime = mainObject.getJSONObject("location").getString("localtime")
        )
    }
    return list
}


