package com.example.myweatherapp2.scrins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myweatherapp2.data.WeatherModel
import com.example.myweatherapp2.ui.theme.PurpleLite
@Composable
fun MainList(list: List<WeatherModel>, currentDays: MutableState<WeatherModel>){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(
            list

        ){
                _,item-> CardTab(item)
        }

    }
}

@Composable
fun CardTab(item: WeatherModel,) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp, end = 7.dp),
        colors = CardDefaults.cardColors(PurpleLite),
        shape = RoundedCornerShape(5.dp),
        elevation =CardDefaults.cardElevation(5.dp),

    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier,
                ) {
                Text(text = item.time,
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )

                Text(text = item.condition,
                    style = TextStyle(
                        fontSize = 17.sp
                    ))
            }

                Text(
                    text = if (item.currentTemp=="") {" ${item.maxTemp}°C/${item.minTemp}°C" }
                        else item.currentTemp+"°C" ,
                    style = TextStyle(fontSize = 20.sp)
                )

            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "image7",
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Composable
fun DialogSearch(dialogState: MutableState<Boolean>,onSubmit: (String)->Unit){
    val dialogText = remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = {
        dialogState.value = false
    },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(dialogText.value)
                dialogState.value = false
            }) {
             Text(text = "OK")   
            }
        }, dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
              Text(text = "Cancel")
            }
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Введите название города: ")
                TextField(value = dialogText.value, onValueChange = {
                    dialogText.value = it
                })
            }

        }
        ) 
}