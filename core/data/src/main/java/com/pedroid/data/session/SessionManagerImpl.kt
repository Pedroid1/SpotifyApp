package com.pedroid.data.session

import com.pedroid.data.local.encriptedstorage.SecureStorage
import com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto
import com.pedroid.data.repository.auth.AuthRepository
import com.pedroid.domain.session.SessionManager
import retrofit2.HttpException
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

        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_CLIENT_SECRET = "client_secret"
    }

    override suspend fun loginWithCode(
        code: String,
        clientId: String,
        clientSecret: String
    ): Result<Unit> {
        return try {
            val token = authRepository.exchangeCodeForToken(code, clientId, clientSecret)
            saveTokenData(token)
            saveCredentials(clientId, clientSecret)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }

    private fun saveCredentials(clientId: String, clientSecret: String) {
        secureStorage.saveString(KEY_CLIENT_ID, clientId)
        secureStorage.saveString(KEY_CLIENT_SECRET, clientSecret)
    }

    override suspend fun refreshAccessToken(): Boolean {
        val refreshToken = secureStorage.getString(KEY_REFRESH_TOKEN)
            ?: return false

        val clientId = secureStorage.getString(KEY_CLIENT_ID)
        val clientSecret = secureStorage.getString(KEY_CLIENT_SECRET)
        if (clientId == null || clientSecret == null) return false

        @Suppress("SwallowedException")
        return try {
            val token = authRepository.refreshAccessToken(refreshToken, clientId, clientSecret)
            saveTokenData(token)
            true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
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

    override suspend fun ensureValidSession(): Boolean {
        if (isLoggedIn()) return true

        val refreshed = refreshAccessToken()
        if (!refreshed) clearSession()

        return refreshed
    }
}
