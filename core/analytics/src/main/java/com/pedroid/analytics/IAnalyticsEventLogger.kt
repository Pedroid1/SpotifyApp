package com.pedroid.analytics

interface IAnalyticsEventLogger {
    fun logEvent(name: String, params: Map<String, Any>? = null)
}
