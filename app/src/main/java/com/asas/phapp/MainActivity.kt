package com.asas.phapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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

    @Suppress("UNCHECKED_CAST")
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

fun refreshValue(
    pHFlow: MutableStateFlow<Resource<Double>>, tempFlow: MutableStateFlow<Resource<Double>>
) {
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            try {
                val response1 = URL("http://192.168.4.1/value1").readText()
                val response2 = URL("http://192.168.4.1/value2").readText()
                val value1 = response1.toDouble()
                val value2 = response2.toDouble()
                pHFlow.emit(Resource.Success(value1))
                tempFlow.emit(Resource.Success(value2))
            } catch (e: Exception) {
                pHFlow.emit(Resource.Error(e))
                tempFlow.emit(Resource.Error(e))
            }
            delay(500)
        }
    }
}

fun REST() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = URL("http://192.168.4.1/rest").readText()
        } catch (e: Exception) {
        }
    }
}


@Composable
fun MainScreen(viewModel: DeviceViewModel) {
    //State Control
    var showDialog by remember { mutableStateOf(false) }
    var place by remember { mutableStateOf("") }
    //list of places
    val places = remember { viewModel.places }
    //realTime Readings
    val phFlow = remember { MutableStateFlow<Resource<Double>>(Resource.Loading) }
    val tempFlow = remember { MutableStateFlow<Resource<Double>>(Resource.Loading) }
    val ph = phFlow.collectAsState()
    val temp = tempFlow.collectAsState()
    var lastPh = remember { 0.0 }
    var lastTemp = remember { 0.0 }

    LaunchedEffect(Unit) {
        refreshValue(phFlow, tempFlow)
    }

    Scaffold(backgroundColor = Color(0xff172732), floatingActionButton = {
        FloatingActionButton(
            onClick = { showDialog = true },
            backgroundColor = Color.Blue,
            contentColor = Color.White
        ) {
//            Icon(Icons.Filled.Add, "")
            Text("أخذ قراءة")
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
            when {
                ph.value is Resource.Success -> {
                    lastPh = (ph.value as Resource.Success).data
                    lastTemp = (temp.value as Resource.Success).data
                    MainItem(ph = "$lastPh",
                        temp = "$lastTemp",
                        onRest = { REST() },
                        onRefresh = { refreshValue(phFlow, tempFlow) })
                }
                ph.value is Resource.Error && temp.value is Resource.Error -> {
                    MainItem(ph = "error",
                        temp = temp.value.toString(),
                        onRest = { REST() },
                        onRefresh = { refreshValue(phFlow, tempFlow) })
                }
                ph.value is Resource.Loading || temp.value is Resource.Loading -> {
                    MainItem(ph = "-1.0",
                        temp = "-1.0",
                        onRest = { REST() },
                        onRefresh = { refreshValue(phFlow, tempFlow) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(places.value) { city ->
                    val valueList = mutableListOf<Reading>()
                    for (read in viewModel.readings.collectAsState().value) {
                        if (read.place == city) {
                            valueList.add(read)
                        }
                    }
                    CityItem(_readingList = valueList as List<Reading>) {
                        viewModel.delete(valueList.last())
                    }
                }
            }
        }
    })

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            title = { Text(text = "إضافة قراءه") },
            confirmButton = {
                Button(onClick = {
                    viewModel.addReading(place, lastPh, lastTemp)
                    showDialog = false
                }) {
                    Text(text = "اضف قراءه")
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
                    OutlinedTextField(value = place, onValueChange = { place = it })
                }
            })
    }
}