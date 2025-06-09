package com.pedroid.data.repository.auth

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.remote.api.auth.AuthApi
import com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var api: AuthApi
    private lateinit var repository: AuthRepositoryImpl

    private val fakeToken = UserAccessTokenDto(
        accessToken = "abc123",
        tokenType = "Bearer",
        expiresIn = 3600,
        tokenRefresh = "refresh456"
    )

    @Before
    fun setUp() {
        api = mockk()
        repository = AuthRepositoryImpl(
            ioDispatcher = Dispatchers.Unconfined,
            authApi = api
        )
    }

    @Test
    fun `exchangeCodeForToken should return token from API`() = runTest {
        coEvery { api.getAccessToken(any(), any(), any(), any(), any()) } returns fakeToken

        val result = repository.exchangeCodeForToken("code123", "clientId", "clientSecret")

        assertThat(result).isEqualTo(fakeToken)
    }

    @Test
    fun `refreshAccessToken should return new token from API`() = runTest {
        coEvery { api.getNewToken(any(), any(), any(), any(), any()) } returns fakeToken

        val result = repository.refreshAccessToken("refresh456", "clientId", "clientSecret")

        assertThat(result).isEqualTo(fakeToken)
    }
}
