package com.pedroid.domain.usecase.playlist

import com.google.common.truth.Truth.assertThat
import com.pedroid.common.core.DataResource
import com.pedroid.domain.repository.PlaylistRepository
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.model.UserProfile
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CreatePlaylistUseCaseImplTest {

    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var useCase: CreatePlaylistUseCase

    @Before
    fun setUp() {
        getUserProfileUseCase = mockk()
        playlistRepository = mockk()
        useCase = CreatePlaylistUseCaseImpl(getUserProfileUseCase, playlistRepository)
    }

    @Test
    fun `execute should create playlist when user profile is available`() = runTest {
        val userProfile = UserProfile(id = "user123", displayName = "João", imageUrl = "url")
        val playlistName = "Nova Playlist"

        coEvery { getUserProfileUseCase.getUserProfile() } returns DataResource.Success(userProfile)
        coEvery { playlistRepository.createPlaylist(userId = userProfile.id, playlistName = playlistName) } returns DataResource.Success(Unit)

        val result = useCase.execute(playlistName)

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
    }

    @Test
    fun `execute should return error when user profile fails`() = runTest {
        val playlistName = "Nova Playlist"
        val exception = IOException("Erro ao buscar perfil")

        coEvery { getUserProfileUseCase.getUserProfile() } returns DataResource.Error(exception)

        val result = useCase.execute(playlistName)

        assertThat(result).isInstanceOf(DataResource.Error::class.java)
        assertThat((result as DataResource.Error).exception).isEqualTo(exception)
    }
}
