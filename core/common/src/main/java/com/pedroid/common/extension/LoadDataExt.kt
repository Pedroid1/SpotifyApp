package com.pedroid.common.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.common.core.DataResource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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

fun <T, S> ViewModel.loadDataResourceFlow(
    initialState: S,
    fetch: suspend () -> DataResource<T>,
    onLoading: S.(Boolean) -> S,
    onSuccess: S.(T) -> S,
    onError: S.(Throwable?) -> S
): StateFlow<S> = flow {
    var currentState = initialState.onLoading(true)
    emit(currentState)

    currentState = when (val result = fetch()) {
        is DataResource.Success -> currentState.onSuccess(result.data)
        is DataResource.Error -> currentState.onError(result.exception)
    }
    emit(currentState)

    currentState = currentState.onLoading(false)
    emit(currentState)
}.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialState
)
