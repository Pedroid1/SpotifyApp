package com.pedroid.eventbus

sealed class AppEvent {
    data object LOGOUT : AppEvent()
}
