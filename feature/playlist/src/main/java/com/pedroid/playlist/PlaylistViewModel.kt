package com.pedroid.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.core.DataResource
import com.pedroid.common.core.UiText
import com.pedroid.common.extension.loadDataResourceFlow
import com.pedroid.common.livedata.Event
import com.pedroid.domain.usecase.playlist.CreatePlaylistUseCase
import com.pedroid.domain.usecase.playlist.GetPlaylistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.playlist.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
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

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    val state: StateFlow<PlaylistUiState> = loadDataResourceFlow(
        initialState = PlaylistUiState(),
        fetch = { getUserProfileUseCase.getUserProfile() },
        onLoading = { copy(isLoading = it) },
        onSuccess = { copy(userProfile = it) },
        onError = {
            _errorEvent.value = Event(UiText.StringResource(R.string.error_fetching_data))
            this
        }
    )

    fun createPlaylist(name: String) = viewModelScope.launch {
        val result = createPlaylistUseCase.execute(name)

        if (result is DataResource.Success) {
            refreshTrigger.value = Event(Unit)
        }
    }
}
