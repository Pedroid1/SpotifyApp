package com.pedroid.domain.repository

import androidx.paging.PagingData
import com.pedroid.model.Album
import com.pedroid.model.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {
    fun getArtists(): Flow<PagingData<Artist>>
    fun getArtistAlbumsById(id: String): Flow<PagingData<Album>>
}