package com.pedroid.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBusController {

    private val _eventBus = MutableSharedFlow<AppEvent>()
    val eventBus = _eventBus.asSharedFlow()

    suspend fun publishEvent(appEvent: AppEvent) {
        _eventBus.emit(appEvent)
    }
}