package com.pedroid.testing.util

import com.pedroid.analytics.IAnalyticsEventLogger

data class LoggedEvent(
    val name: String,
    val params: Map<String, Any>?
)

class TestAnalyticsHelper : IAnalyticsEventLogger {

    private val _events = mutableListOf<LoggedEvent>()
    val events: List<LoggedEvent> get() = _events

    fun hasLogged(name: String, params: Map<String, Any>? = null): Boolean {
        return _events.any { it.name == name && (params == null || it.params == params) }
    }

    override fun logEvent(name: String, params: Map<String, Any>?) {
        _events.add(LoggedEvent(name, params))
    }
}
