package com.asas.phapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CityItem(_readingList: List<Reading>, onDelete: () -> Unit) {
    var readingList = listOf<Reading>()
    readingList = if(_readingList.isNullOrEmpty()){
        listOf<Reading>(Reading(place = "",reading = 0.0, temp = 0.0))
    }else{
        _readingList
    }
    var expanded by remember { mutableStateOf(false) }
    val reading = readingList.last()
    val city = reading.place
    val ph = reading.reading
    val temp = reading.temp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color(0xff2F405A), shape = RoundedCornerShape(32.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                painter = painterResource(id = R.drawable.game_icons_h2o),
                contentDescription = "nice icon for the app",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(Modifier.width(50.dp))
            Text(
                text = city, style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.cairo_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Right,
                ), color = Color.White, modifier = Modifier
                    .padding(end = 16.dp)
                    .wrapContentSize()
            )

        }
        Text(
            text = "$ph PH معدل",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.cairo_regular)),
                fontWeight = FontWeight(500),
            ),
            color = Color.White,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Text(
            text = " $temp درجة الحرارة",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.cairo_regular)),
                fontWeight = FontWeight(500),
            ),
            color = Color.White,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onDelete, modifier = Modifier.size(50.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xff172732),
                )
            }
            Box() {
                Text(
                    "PH:${readingList.last().reading} Temperature:${readingList.last().temp}",
                    modifier = Modifier
                        .clickable(onClick = { expanded = true })
                        .background(
                            Color(0xff2F405A)
                        ),
                    style = TextStyle(color = Color.White, fontSize = 24.sp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(
                        Color(0xff172732)
                    ),
                    offset = DpOffset.Zero
                ) {
                    readingList.forEach { s ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text(
                                "ph${s.reading},temp ${s.temp}",
                                modifier = Modifier
                                    .clickable(onClick = { expanded = true })
                                    .background(
                                        Color(0xff172732)
                                    ),
                                style = TextStyle(color = Color.White, fontSize = 24.sp)
                            )
                        }
                    }
                }
            }
            IconButton(onClick = {
                expanded = true
            }, modifier = Modifier.size(100.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Delete",
                    tint = Color.White,
                )
            }
        }
    }
}