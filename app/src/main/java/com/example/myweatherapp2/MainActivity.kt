package com.example.myweatherapp2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myweatherapp2.data.WeatherModel
import com.example.myweatherapp2.scrins.CardTab
import com.example.myweatherapp2.scrins.DialogSearch
import com.example.myweatherapp2.scrins.MainCard
import com.example.myweatherapp2.scrins.TabLayout
import org.json.JSONObject

const val API_KEY = "36a1ac531c0f48cc97f51319242304"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val daysList = remember {
                mutableStateOf(listOf<WeatherModel>())
            }
            val dialogState = remember {
                mutableStateOf(false)
            }
            val currentDay = remember {
            mutableStateOf(WeatherModel(
                "",
                "",
                "0.0",
                "",
                "",
                "0.0",
                "0.0",
                "",

            )
            )
        }
            if(dialogState.value){
                DialogSearch(dialogState, onSubmit = {
                    getData(it, this,daysList,currentDay)
                })
            }

            getData("Иркутск", this,daysList,currentDay)
            Image(
                painter = painterResource(id = R.drawable._139510_1000n),
                contentDescription = "Image1",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(1.0f),
                contentScale = ContentScale.FillBounds
            )
            Column {

                MainCard(currentDay, onClickSync = {
                    getData("Иркутск", this@MainActivity,daysList,currentDay)
                },
                    onClickSearch = {
                    dialogState.value = true
                })
                TabLayout(daysList,currentDay)

            }
        }
    }
}

private fun getData(citi: String,
                    context: Context,
                    daysList: MutableState<List<WeatherModel>>,
                    currentDay: MutableState<WeatherModel>) {
    val url =
        "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY" +
                "&q=$citi" +
                "&days=" +
                "3" +
                "&aqi=no" +
                "&alerts=no"+
                "&lang=ru"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url,
        { response -> val resp = String(response.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)

            val list = getWeatherByDays(resp)
            currentDay.value = list[0]
            daysList.value = list
        },
        {
            Log.d("MyLog", "VollyERROR: $it")
        }
    )
    queue.add(sRequest)
}

private fun getWeatherByDays(response: String): List<WeatherModel>{
    if (response.isEmpty()) return listOf()
    val list = ArrayList<WeatherModel>()
    val mainObject = JSONObject(response)

    val city = mainObject.getJSONObject("location").getString("name")
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()){
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("text"),
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                item.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                item.getJSONArray("hour").toString(),


            )
        )
    }
    list[0] = list[0].copy(
        time = mainObject.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c").toFloat().toInt().toString(),
        condition = mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
        icon = mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
    )
    return list
}
