package com.asas.phapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainItem(city: String, ph: Double) {
    val context = LocalContext.current
    val mySharedPreferences = SharedPrefs(context)
    var openDialog by remember { mutableStateOf(false) }
    var place by remember { mutableStateOf(city) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xff2F405A))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Image(
                painter = painterResource(id = R.drawable.material_symbols_water_ph),
                contentDescription = "ph icon",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "$place", style = TextStyle(
                    fontSize = 64.sp,
                    fontFamily = FontFamily(Font(R.font.cairo_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ), color = Color.White, modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    .combinedClickable(onLongClick = { openDialog = true }, onClick = { /*....*/ }),
            )

        }
        Text(
            text = "$ph PH معدل",
            style = TextStyle(
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.cairo_regular)),
                fontWeight = FontWeight(500),
            ),
            color = Color.White,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
        )
    }

    if (openDialog) {
        AlertDialog(onDismissRequest = { openDialog = false },
            title = { Text(text = "تغيير الاسم") },
            text = {
                Column {
                    TextField(value = city, onValueChange = { place = it })
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Set the title of the widget to the text entered by the user
                    // ...
                    mySharedPreferences.saveTitle(city)
                    openDialog = false
                }) {
                    Text("موافق")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog = false }) {
                    Text("الغاء")
                }
            })
    }
}
