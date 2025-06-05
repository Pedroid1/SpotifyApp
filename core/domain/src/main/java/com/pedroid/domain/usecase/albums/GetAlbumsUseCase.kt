package com.pedroid.domain.usecase.albums

import androidx.paging.PagingData
import com.pedroid.model.Album
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    fun execute(artistId: String): Flow<PagingData<Album>>
}