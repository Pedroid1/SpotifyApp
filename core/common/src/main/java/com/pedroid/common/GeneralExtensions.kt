package com.pedroid.common

import android.app.Service
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import java.io.Serializable

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}

fun String.capitalize() = this.replaceFirstChar { it.uppercase() }

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(key)?.let {
            clazz.cast(it)
        }
    }
}
