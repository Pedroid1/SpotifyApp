package com.pedroid.data.session

import com.pedroid.data.local.encriptedstorage.SecureStorage
import com.pedroid.data.remote.auth.dto.UserAccessTokenDto
import com.pedroid.data.repository.auth.AuthRepository
import com.pedroid.domain.session.SessionManager
import java.io.IOException
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    private val secureStorage: SecureStorage,
    private val authRepository: AuthRepository
) : SessionManager {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
    }

    override suspend fun loginWithCode(code: String): Result<Unit> {
        return try {
            val token = authRepository.exchangeCodeForToken(code)
            saveTokenData(token)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun refreshAccessToken(): Boolean {
        val refreshToken = secureStorage.getString(KEY_REFRESH_TOKEN)
            ?: return false

        @Suppress("SwallowedException")
        return try {
            val token = authRepository.refreshAccessToken(refreshToken)
            saveTokenData(token)
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun getAccessToken(): String? = secureStorage.getString(KEY_ACCESS_TOKEN)

    override fun isLoggedIn(): Boolean {
        val token = secureStorage.getString(KEY_ACCESS_TOKEN)
        val expiresAt = secureStorage.getLong(KEY_EXPIRES_AT) ?: return false
        return token != null && System.currentTimeMillis() < expiresAt
    }

    override fun clearSession() {
        secureStorage.clear()
    }

    private fun saveTokenData(token: UserAccessTokenDto) {
        val expiresAt = System.currentTimeMillis() + (token.expiresIn * 1000L)
        secureStorage.saveString(KEY_ACCESS_TOKEN, token.accessToken.orEmpty())
        secureStorage.saveString(KEY_REFRESH_TOKEN, token.tokenRefresh.orEmpty())
        secureStorage.saveLong(KEY_EXPIRES_AT, expiresAt)
    }
}
