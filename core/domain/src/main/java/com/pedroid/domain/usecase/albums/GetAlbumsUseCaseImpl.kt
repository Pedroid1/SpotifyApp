package com.pedroid.domain.usecase.albums

import androidx.paging.PagingData
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.model.Album
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCaseImpl @Inject constructor(
    private val artistsRepository: ArtistsRepository
) : GetAlbumsUseCase {
    override fun execute(artistId: String): Flow<PagingData<Album>> = artistsRepository.getArtistAlbumsById(artistId)
}