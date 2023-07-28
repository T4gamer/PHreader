package com.asas.phapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainItem(ph: String, temp: String, onRefresh: () -> Unit,onRest:()->Unit) {
    val city = "القراءه الحالية"
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
                text = "$city",
                style = TextStyle(
                    fontSize = 64.sp,
                    fontFamily = FontFamily(Font(R.font.cairo_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    .combinedClickable(onLongClick = onRest, onClick = onRefresh),
            )
        }
        when (ph) {
            "error" -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("connection failed")
                Button(onClick = onRefresh) {
                    Text(text = "try again")
                }
            }
            "-1.0" -> Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> {
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
                Text(
                    text = "$temp درجة الحرارة ",
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
        }
    }
}
