package com.pedroid.privatemodule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.domain.usecase.albums.GetAlbumsUseCase
import com.pedroid.model.Artist
import com.pedroid.privatemodule.navigation.AlbumsFeatureCommunicatorImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val artist: Artist = requireNotNull(
        savedStateHandle.get<Artist>(AlbumsFeatureCommunicatorImpl.ARTIST_KEY)
    )

    val albums = getAlbumsUseCase.execute(artist.id).cachedIn(viewModelScope)

    val uiState: StateFlow<AlbumsUiState> = flow {
        emit(AlbumsUiState(artist = artist, isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlbumsUiState(isLoading = true)
    )

}