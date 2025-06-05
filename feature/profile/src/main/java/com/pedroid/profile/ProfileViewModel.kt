package com.pedroid.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.common.ClickUtil
import com.pedroid.common.DataResource
import com.pedroid.common.UiText
import com.pedroid.common.livedata.Event
import com.pedroid.domain.session.SessionManager
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.eventbus.AppEvent
import com.pedroid.eventbus.EventBusController
import com.pedroid.feature.profile.R
import com.pedroid.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val eventBusController: EventBusController,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile get() = _userProfile

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    init {
        getUserData()
    }

    fun logout() {
        if (ClickUtil.isFastDoubleClick) return
        //sessionManager.clearSession()
        viewModelScope.launch {
            eventBusController.publishEvent(AppEvent.LOGOUT)
        }
    }

    private fun getUserData() = viewModelScope.launch {
        _isLoading.postValue(true)
        when (val result = getUserProfileUseCase.getUserProfile()) {
            is DataResource.Success -> {
                _userProfile.postValue(result.data)
            }

            is DataResource.Error -> {
                errorEvent.postValue(Event(UiText.StringResource(R.string.error_fetching_data)))
            }
        }
        _isLoading.postValue(false)
    }
}