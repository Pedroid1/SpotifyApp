package com.pedroid.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.DataResource
import com.pedroid.common.UiText
import com.pedroid.common.livedata.Event
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCase
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.playlist.R
import com.pedroid.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistUseCase: GetPlaylistsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase
) : ViewModel() {

    val playlists = playlistUseCase.execute().cachedIn(viewModelScope)

    private val _refreshTrigger = MutableLiveData<Event<Unit>>()
    val refreshTrigger get() = _refreshTrigger

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

    fun createPlaylist(name: String) = viewModelScope.launch {
        val result = createPlaylistUseCase.execute(name)

        if (result is DataResource.Success) {
            refreshTrigger.postValue(Event(Unit))
        }
    }
}