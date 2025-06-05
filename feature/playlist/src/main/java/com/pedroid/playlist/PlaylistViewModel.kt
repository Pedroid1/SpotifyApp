package com.pedroid.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.DataResource
import com.pedroid.common.UiText
import com.pedroid.common.livedata.Event
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.playlist.R
import com.pedroid.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistUseCase: GetPlaylistsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val playlists = playlistUseCase.execute()
        .cachedIn(viewModelScope)

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile get() = _userProfile

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    init {
        getUserData()
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