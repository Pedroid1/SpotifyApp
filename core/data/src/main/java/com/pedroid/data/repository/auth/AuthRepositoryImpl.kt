package com.pedroid.data.repository.auth

import com.pedroid.data.remote.auth.AuthApi
import com.pedroid.data.remote.auth.dto.UserAccessTokenDto
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun exchangeCodeForToken(code: String): UserAccessTokenDto {
        return authApi.getAccessToken(code = code)
    }

    override suspend fun refreshAccessToken(refreshToken: String): UserAccessTokenDto {
        return authApi.getNewToken(refreshToken = refreshToken)
    }
}
