package com.pedroid.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun <T> ViewModel.loadDataResource(
    fetch: suspend () -> DataResource<T>,
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Throwable?) -> Unit
) {
    viewModelScope.launch {
        onLoading(true)
        when (val result = fetch()) {
            is DataResource.Success -> onSuccess(result.data)
            is DataResource.Error -> onError(result.exception)
        }
        onLoading(false)
    }
}
