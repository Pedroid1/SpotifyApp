package com.pedroid.domain.repository

import androidx.paging.PagingData
import com.pedroid.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<PagingData<Playlist>>
}