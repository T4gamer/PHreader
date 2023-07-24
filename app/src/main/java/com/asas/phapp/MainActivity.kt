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
import androidx.compose.ui.platform.LocalContext
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
                ) {
                    MainScreen()
                }
            }
        }
    }
}

fun refreshValue(pHFlow: MutableStateFlow<Resource<Double>>) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response =
                URL("https://blynk.cloud/external/api/get?token=cLolM734TLYcbjBCCh97oDB1SN9e4g1z&v5").readText()
            val value = response.toDoubleOrNull() ?: 0.0
            pHFlow.emit(Resource.Success(value))
        } catch (e: Exception) {
            pHFlow.emit(Resource.Error(e))
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val mySharedPreferences = SharedPrefs(context)
    var city = mySharedPreferences.getTitle()
    val pH = remember { mutableStateOf<Resource<Double>>(Resource.Loading) }
    val pHFlow = remember { MutableStateFlow<Resource<Double>>(Resource.Loading) }
    LaunchedEffect(Unit) {
        refreshValue(pHFlow)
        pHFlow.collect { pH.value = it }
    }
    if(city.isNullOrEmpty()){
        city = "طرابلس";
    }
    Column(
        modifier = Modifier
            .background(color = Color(0xff172732))
            .fillMaxWidth()
            .fillMaxHeight(0.25f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val resource = pH.value) {
            is Resource.Success -> MainItem(city = city, ph = resource.data)
            is Resource.Error -> MainItem(city = city, ph = -1.0)
            is Resource.Loading -> CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { refreshValue(pHFlow) }) {
            Text(text = "Refresh")
        }
    }
}


