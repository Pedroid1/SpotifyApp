package com.pedroid.domain.repository

import androidx.paging.PagingData
import com.pedroid.model.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {
    suspend fun getArtists(): Flow<PagingData<Artist>>
}