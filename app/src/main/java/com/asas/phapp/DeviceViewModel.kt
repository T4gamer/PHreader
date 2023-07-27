package com.asas.phapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class DeviceViewModel(private val dao: PHDao) : ViewModel() {
    private val _readings = MutableStateFlow<List<Reading>>(emptyList())
    init {
        getReadings()
    }

    fun logData() {
        Log.d("datum",_readings.value.toString())
    }
    fun addReading(place: String, reading: Double) {
        if (place.isBlank()) {
            viewModelScope.launch {
                dao.insert(Reading(place = "طرابلس", reading = reading))
            }
        }
        viewModelScope.launch {
            dao.insert(Reading(place = place, reading = 0.0))
        }
    }

    fun getReadings(): List<Reading> {
        viewModelScope.launch {
            dao.getAll().collect { devices ->
                _readings.value = devices
            }
        }
        logData()
        return _readings.value
    }
    fun deleteAll(){
        viewModelScope.launch {
            for(reading in _readings.value){
                dao.delete(reading)
            }
        }
    }
}
