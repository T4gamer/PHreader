package com.asas.phapp

sealed interface DeviceEvent {
    object SaveDevice : DeviceEvent
    data class SetToken(val token: String) : DeviceEvent
    data class SetPlace(val place: String) : DeviceEvent
    object ShowDialog : DeviceEvent
    object HideDialog : DeviceEvent
}