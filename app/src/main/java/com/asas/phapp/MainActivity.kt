package com.asas.phapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asas.phapp.ui.theme.PhReaderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xff172732)),
                    color = Color(0xff172732)
//                    color = MaterialTheme.colors.background
                ) {
                    MyScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PhReaderTheme {
    }
}

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
@Composable
fun MyScreen() {
    val pH = remember { mutableStateOf<Resource<Double>>(Resource.Loading) }
    val pHFlow = remember { MutableStateFlow<Resource<Double>>(Resource.Loading) }
    fun refreshValue() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = URL("https://blynk.cloud/external/api/get?token=cLolM734TLYcbjBCCh97oDB1SN9e4g1z&v5").readText()
                val value = response.toDoubleOrNull() ?: 0.0
                pHFlow.emit(Resource.Success(value))
            } catch (e: Exception) {
                pHFlow.emit(Resource.Error(e))
            }
        }
    }
    LaunchedEffect(Unit) {
        refreshValue()
        pHFlow.collect { pH.value = it }
    }


    Column(
        modifier = Modifier
            .background(color = Color(0xff172732))
            .fillMaxWidth()
            .fillMaxHeight(0.25f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val resource = pH.value) {
            is Resource.Success -> MainItem(city = "الزاوية", ph = resource.data)
            is Resource.Error -> Text(text = "Error: ${resource.exception.message}")
            is Resource.Loading -> CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(16.dp))
        ListItem(city = "طرابلس", ph = 8.0)
        Button(onClick = { refreshValue() }) {
            Text(text = "Refresh")
        }
    }
}



@Composable
fun MainItem(city: String, ph: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xff2F405A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Image(
                painter = painterResource(id = R.drawable.material_symbols_water_ph),
                contentDescription = "ph icon",
                modifier = Modifier.size(80.dp).align(Alignment.CenterVertically)
            )
            Text(
                text = city,
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
            )

        }
        Text(
            text = "$ph PH معدل",
            style =TextStyle(
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

@Composable
fun ListItem(city: String, ph: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color(0xff2F405A), shape = RoundedCornerShape(32.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.game_icons_h2o),
//                contentDescription = "nice icon for the app",
//                modifier = Modifier.size(80.dp).align(Alignment.CenterVertically)
//            )
            Spacer(Modifier.width(50.dp))
            Text(
                text = city,
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.cairo_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Right,
                ),
                color = Color.White,
                modifier = Modifier
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

