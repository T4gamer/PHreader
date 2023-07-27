package com.asas.phapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.asas.phapp.ui.theme.PhReaderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext, PHDatabase::class.java, "ph.db"
        ).build()
    }
    private val viewModel by viewModels<DeviceViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DeviceViewModel(db.PHDao()) as T
            }
        }
    })

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
                    MainScreen(viewModel)
                }
            }
        }
    }
}

fun refreshValue(pHFlow: MutableStateFlow<Resource<Double>>) {
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            try {
                pHFlow.emit(Resource.Loading)
                val response = URL("http://192.168.4.1:80/value1").readText()
                val value = response.toDoubleOrNull() ?: 0.0
                pHFlow.emit(Resource.Success(value))
            } catch (e: Exception) {
                pHFlow.emit(Resource.Error(e))
            }
            delay(500)
        }
    }
}


@Composable
fun MainScreen(viewModel: DeviceViewModel) {

    var readings = remember { viewModel.getReadings() }
    val pHFlow = remember { MutableStateFlow<Resource<Double>>(Resource.Loading) }
    var place by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Log.d("datumR", readings.toString())

    LaunchedEffect(Unit) {
        refreshValue(pHFlow)
    }

    Scaffold(backgroundColor = Color(0xff172732), floatingActionButton = {
        FloatingActionButton(
            onClick = { showDialog = true },
            backgroundColor = Color.Blue,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }, content = { padding ->
        Column(
            modifier = Modifier
                .background(color = Color(0xff172732))
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val resource = pHFlow.collectAsState().value) {
                is Resource.Success -> MainItem(city = "القراءه الحالية", ph = resource.data)
                is Resource.Error -> MainItem(city = "القرءاه الحالية", ph = -1.0)
                is Resource.Loading -> CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(readings) { reading ->
                    ListItem(reading.place, reading.reading)
                }
            }
        }
    })


    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            title = { Text(text = "add Device") },
            confirmButton = {
                Button(onClick = {
                    viewModel.addReading(place, 0.0)
                    showDialog = false
                }) {
                    Text(text = "اضف الجهاز")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "الغاء")
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(value = place,
                        onValueChange = { place = it },
                        label = { Text(text = "المكان") })
                }
            })
    }
}



