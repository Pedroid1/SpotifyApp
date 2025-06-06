package com.pedroid.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.core.DataResource
import com.pedroid.common.core.UiText
import com.pedroid.common.extension.loadDataResource
import com.pedroid.common.livedata.Event
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCase
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.playlist.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    init {
        loadDataResource(
            fetch = { getUserProfileUseCase.getUserProfile() },
            onLoading = { isLoading ->
                _uiState.update { it.copy(isLoading = isLoading) }
            },
            onSuccess = { user -> _uiState.update { it.copy(userProfile = user) } },
            onError = { _errorEvent.postValue(Event(UiText.StringResource(R.string.error_fetching_data))) }
        )
    }

    fun createPlaylist(name: String) = viewModelScope.launch {
        val result = createPlaylistUseCase.execute(name)

        if (result is DataResource.Success) {
            refreshTrigger.postValue(Event(Unit))
        }
    }
}
