package com.pedroid.domain.usecase.artist

import androidx.paging.PagingData
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.model.Artist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistsUseCaseImpl @Inject constructor(
    private val repository: ArtistsRepository
) : GetArtistsUseCase {

    override fun execute(): Flow<PagingData<Artist>> = repository.getArtists()
}