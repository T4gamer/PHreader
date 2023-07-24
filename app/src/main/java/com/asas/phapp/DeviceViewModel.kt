package com.asas.phapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceViewModel(private val dao: PHDao) : ViewModel() {
    private val _state = MutableStateFlow(DeviceState())


    fun onEvent(event: DeviceEvent) {
        when (event) {
            DeviceEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingDevice = false
                    )
                }
            }
            DeviceEvent.SaveDevice -> {
                val place = _state.value.place
                val token = _state.value.token
                if (place.isBlank() || token.isBlank()) {
                    return
                }
                viewModelScope.launch {
                    dao.insert(Device(id = 0, place = place, token = token))
                }
            }
            is DeviceEvent.SetPlace -> {
                _state.update {
                    it.copy(
                        place = event.place
                    )
                }
            }
            is DeviceEvent.SetToken -> {
                _state.update {
                    it.copy(
                        token = event.token
                    )
                }
            }
            DeviceEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingDevice = true
                    )
                }
            }
        }
    }
}