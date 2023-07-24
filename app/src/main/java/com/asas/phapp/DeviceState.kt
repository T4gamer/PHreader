package com.asas.phapp

data class DeviceState(
    val devices: List<Device> = emptyList(),
    val token: String = "",
    val place: String = "",
    val isAddingDevice: Boolean = false
)