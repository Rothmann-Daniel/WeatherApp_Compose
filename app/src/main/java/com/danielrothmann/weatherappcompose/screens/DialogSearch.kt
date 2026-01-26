package com.danielrothmann.weatherappcompose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DialogSearch(
    dialogState: MutableState<Boolean>,
    onCitySelected: (String) -> Unit
) {
    val cityName = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (cityName.value.isNotBlank()) {
                        onCitySelected(cityName.value)
                    }
                    dialogState.value = false
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter your City")
                TextField(
                    value = cityName.value,
                    onValueChange = { cityName.value = it },
                    placeholder = { Text("City name") }
                )
            }
        }
    )
}

