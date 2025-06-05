package com.pedroid.designsystem.components.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.isVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
