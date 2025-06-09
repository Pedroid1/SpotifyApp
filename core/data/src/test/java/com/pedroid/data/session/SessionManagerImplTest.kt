package com.pedroid.data.session

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.local.encriptedstorage.SecureStorage
import com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto
import com.pedroid.data.repository.auth.AuthRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SessionManagerImplTest {

    private lateinit var secureStorage: SecureStorage
    private lateinit var authRepository: AuthRepository
    private lateinit var sessionManager: SessionManagerImpl

    private val testToken = UserAccessTokenDto(
        accessToken = "abc123",
        tokenType = "Bearer",
        expiresIn = 3600,
        tokenRefresh = "refresh456",
    )

    @Before
    fun setUp() {
        secureStorage = mockk(relaxed = true)
        authRepository = mockk()
        sessionManager = SessionManagerImpl(secureStorage, authRepository)
    }

    @Test
    fun `loginWithCode should return success and save token`() = runTest {
        coEvery { authRepository.exchangeCodeForToken(any(), any(), any()) } returns testToken

        val result = sessionManager.loginWithCode("code", "clientId", "clientSecret")

        assertThat(result.isSuccess).isTrue()
        verify { secureStorage.saveString("access_token", "abc123") }
        verify { secureStorage.saveString("refresh_token", "refresh456") }
        verify { secureStorage.saveLong("expires_at", more(0L)) }
    }

    @Test
    fun `loginWithCode should return failure on IOException`() = runTest {
        coEvery { authRepository.exchangeCodeForToken(any(), any(), any()) } throws IOException()

        val result = sessionManager.loginWithCode("code", "clientId", "clientSecret")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `refreshAccessToken should return true and save token when successful`() = runTest {
        every { secureStorage.getString(any()) } returns "refresh456"
        coEvery { authRepository.refreshAccessToken(any(), any(), any()) } returns testToken

        val result = sessionManager.refreshAccessToken()

        assertThat(result).isTrue()
        verify { secureStorage.saveString("access_token", "abc123") }
    }

    @Test
    fun `refreshAccessToken should return false if refreshToken is null`() = runTest {
        every { secureStorage.getString(any()) } returns null

        val result = sessionManager.refreshAccessToken()

        assertThat(result).isFalse()
    }

    @Test
    fun `refreshAccessToken should return false on IOException`() = runTest {
        every { secureStorage.getString(any()) } returns "refresh456"
        coEvery { authRepository.refreshAccessToken(any(), any(), any()) } throws IOException()

        val result = sessionManager.refreshAccessToken()

        assertThat(result).isFalse()
    }

    @Test
    fun `getAccessToken should return stored value`() {
        every { secureStorage.getString(any()) } returns "abc123"

        val token = sessionManager.getAccessToken()

        assertThat(token).isEqualTo("abc123")
    }

    @Test
    fun `isLoggedIn should return true when token is valid`() {
        every { secureStorage.getString(any()) } returns "abc123"
        every { secureStorage.getLong(any()) } returns System.currentTimeMillis() + 60_000

        val result = sessionManager.isLoggedIn()

        assertThat(result).isTrue()
    }

    @Test
    fun `isLoggedIn should return false when token is expired`() {
        every { secureStorage.getString(any()) } returns "abc123"
        every { secureStorage.getLong(any()) } returns System.currentTimeMillis() - 1

        val result = sessionManager.isLoggedIn()

        assertThat(result).isFalse()
    }

    @Test
    fun `clearSession should call secureStorage clear`() {
        every { secureStorage.clear() } just Runs

        sessionManager.clearSession()

        verify { secureStorage.clear() }
    }

    @Test
    fun `ensureValidSession returns true when already logged in`() = runTest {
        every { secureStorage.getString(any()) } returns "abc123"
        every { secureStorage.getLong(any()) } returns System.currentTimeMillis() + 60_000

        val result = sessionManager.ensureValidSession()

        assertThat(result).isTrue()
    }

    @Test
    fun `ensureValidSession refreshes token if not logged in`() = runTest {
        every { secureStorage.getString(any()) } returns null
        every { secureStorage.getLong(any()) } returns null
        every { secureStorage.getString(any()) } returns "refresh456"
        coEvery { authRepository.refreshAccessToken(any(), any(), any()) } returns testToken

        val result = sessionManager.ensureValidSession()

        assertThat(result).isTrue()
        verify { secureStorage.saveString("access_token", "abc123") }
    }

    @Test
    fun `ensureValidSession clears session if refresh fails`() = runTest {
        every { secureStorage.getString(any()) } returns null
        every { secureStorage.getLong(any()) } returns null
        every { secureStorage.getString(any()) } returns "refresh456"
        coEvery { authRepository.refreshAccessToken(any(), any(), any()) } throws IOException()
        every { secureStorage.clear() } just Runs

        val result = sessionManager.ensureValidSession()

        assertThat(result).isFalse()
        verify { secureStorage.clear() }
    }
}
