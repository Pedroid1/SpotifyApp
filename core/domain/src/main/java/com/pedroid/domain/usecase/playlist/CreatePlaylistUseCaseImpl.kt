package com.pedroid.domain.usecase.playlist

import com.pedroid.common.core.DataResource
import com.pedroid.domain.repository.PlaylistRepository
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import javax.inject.Inject

class CreatePlaylistUseCaseImpl @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val playlistRepository: PlaylistRepository
) : CreatePlaylistUseCase {

    override suspend fun execute(name: String): DataResource<Unit> {
        return when (val result = getUserProfileUseCase.getUserProfile()) {
            is DataResource.Success -> {
                playlistRepository.createPlaylist(
                    userId = result.data.id,
                    playlistName = name
                )
            }

            is DataResource.Error -> DataResource.Error(result.exception)
        }
    }
}
