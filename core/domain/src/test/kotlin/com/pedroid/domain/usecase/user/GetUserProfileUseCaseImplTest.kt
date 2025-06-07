package com.pedroid.domain.usecase.user

import com.google.common.truth.Truth.assertThat
import com.pedroid.common.core.DataResource
import com.pedroid.domain.repository.ProfileRepository
import com.pedroid.model.UserProfile
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetUserProfileUseCaseImplTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var useCase: GetUserProfileUseCase

    @Before
    fun setUp() {
        profileRepository = mockk()
        useCase = GetUserProfileUseCaseImpl(profileRepository)
    }

    @Test
    fun `getUserProfile should return success with user profile`() = runTest {
        val expectedProfile = UserProfile(
            id = "user123",
            displayName = "João",
            imageUrl = "url"
        )

        coEvery { profileRepository.getUserProfile() } returns DataResource.Success(expectedProfile)

        val result = useCase.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
        assertThat((result as DataResource.Success).data).isEqualTo(expectedProfile)
    }

    @Test
    fun `getUserProfile should return error when repository fails`() = runTest {
        val exception = IOException("Erro ao buscar perfil")

        coEvery { profileRepository.getUserProfile() } returns DataResource.Error(exception)

        val result = useCase.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Error::class.java)
        assertThat((result as DataResource.Error).exception).isEqualTo(exception)
    }
}
