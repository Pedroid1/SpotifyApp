package com.pedroid.domain.usecase.playlist

import androidx.paging.PagingData
import com.pedroid.model.Playlist
import kotlinx.coroutines.flow.Flow

interface GetPlaylistsUseCase {
    fun execute(): Flow<PagingData<Playlist>>
}
