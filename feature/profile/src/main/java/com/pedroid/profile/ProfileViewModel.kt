package com.pedroid.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.common.core.UiText
import com.pedroid.common.extension.loadDataResourceFlow
import com.pedroid.common.livedata.Event
import com.pedroid.domain.session.SessionManager
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.eventbus.AppEvent
import com.pedroid.eventbus.EventBusController
import com.pedroid.feature.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val eventBusController: EventBusController,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    val state: StateFlow<ProfileUiState> = loadDataResourceFlow(
        initialState = ProfileUiState(),
        fetch = { getUserProfileUseCase.getUserProfile() },
        onLoading = { copy(isLoading = it) },
        onSuccess = { copy(userProfile = it) },
        onError = {
            _errorEvent.value = Event(UiText.StringResource(R.string.error_fetching_data))
            this
        }
    )

    fun logout() {
        sessionManager.clearSession()
        viewModelScope.launch {
            eventBusController.publishEvent(AppEvent.LOGOUT)
        }
    }
}
