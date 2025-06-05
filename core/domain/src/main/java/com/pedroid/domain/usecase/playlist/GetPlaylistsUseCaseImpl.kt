package com.pedroid.domain.usecase.playlist

import androidx.paging.PagingData
import com.pedroid.domain.repository.PlaylistRepository
import com.pedroid.model.Playlist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaylistsUseCaseImpl @Inject constructor(
    private val repository: PlaylistRepository
) : GetPlaylistsUseCase {

    override fun execute(): Flow<PagingData<Playlist>> = repository.getPlaylists()
}