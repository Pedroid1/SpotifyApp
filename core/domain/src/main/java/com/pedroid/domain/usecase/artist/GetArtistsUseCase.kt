package com.pedroid.domain.usecase.artist

import androidx.paging.PagingData
import com.pedroid.model.Artist
import kotlinx.coroutines.flow.Flow

interface GetArtistsUseCase {
    fun execute(): Flow<PagingData<Artist>>
}