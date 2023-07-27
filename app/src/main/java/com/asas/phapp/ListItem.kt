package com.asas.phapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItem(reading:Reading, onDelete:()->Unit) {
    val city = reading.place
    val ph = reading.reading
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
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.game_icons_h2o),
//                contentDescription = "nice icon for the app",
//                modifier = Modifier.size(80.dp).align(Alignment.CenterVertically)
//            )
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
    }
}