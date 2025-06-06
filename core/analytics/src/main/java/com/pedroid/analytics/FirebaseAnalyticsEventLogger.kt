package com.pedroid.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsEventLogger @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : IAnalyticsEventLogger {

    override fun logEvent(name: String, params: Map<String, Any>?) {
        val bundle = Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> throw IllegalArgumentException("Unsupported type for analytics param: $key")
            }
        }
        firebaseAnalytics.logEvent(name, bundle)
    }
}
