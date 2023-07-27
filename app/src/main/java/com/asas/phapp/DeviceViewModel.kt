package com.asas.phapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DeviceViewModel(private val dao: PHDao) : ViewModel() {
    private val _readings = MutableStateFlow<List<Reading>>(emptyList())
    val readings: StateFlow<List<Reading>> = _readings.asStateFlow()

    init {
        getReadings()
    }
    fun addReading(place: String, reading: Double ,temp:Double) {
        viewModelScope.launch {
            val read = Reading(place = place, reading = reading , temp =temp)
            dao.insert(read)
        }
    }

    fun getReadings(): List<Reading> {
        viewModelScope.launch {
            dao.getAll().collect { readings ->
                _readings.emit(readings)
                Log.d("datumReal",readings.toString())
            }
        }
        Log.d("datumReading",_readings.value.toString())
        return _readings.value
    }
    fun deleteAll(){
        viewModelScope.launch {
            for(reading in _readings.value){
                dao.delete(reading)
            }
        }
    }
    fun delete(reading :Reading){
        viewModelScope.launch {
            dao.delete(reading)
        }
    }
}
