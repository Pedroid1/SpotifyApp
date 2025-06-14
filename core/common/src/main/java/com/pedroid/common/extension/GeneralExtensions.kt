package com.pedroid.common.extension

import android.app.Service
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.pedroid.common.utils.GeneralUtils
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

fun View.showSnackBar(message: String, timeLength: Int, anchorView: Int? = null) {
    val snackBar = Snackbar.make(this, message, timeLength)
    anchorView?.let { snackBar.setAnchorView(anchorView) }
    snackBar.show()
}

fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    isCircular: Boolean = false
) {
    this.load(url) {
        crossfade(true)
        placeholder?.let { placeholder(it) }
        error?.let { error(it) }

        if (isCircular) {
            transformations(CircleCropTransformation())
        }
    }
}

fun Fragment.setUserProfile(
    imageView: ImageView,
    initialsView: TextView,
    displayName: String,
    imageUrl: String?,
    backgroundColorResId: Int
) {
    val showImage = !imageUrl.isNullOrBlank()
    val initials = GeneralUtils.getInitials(displayName)

    if (showImage) {
        imageView.backgroundTintList = null
        imageView.background = null
        initialsView.visibility = View.GONE
        imageView.loadImage(url = imageUrl!!)
    } else {
        imageView.setBackgroundColor(
            ContextCompat.getColor(requireContext(), backgroundColorResId)
        )
        initialsView.visibility = View.VISIBLE
        initialsView.text = initials
    }
}
