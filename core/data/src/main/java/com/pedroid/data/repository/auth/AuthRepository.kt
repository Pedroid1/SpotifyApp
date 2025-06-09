package com.pedroid.data.repository.auth

import com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto

interface AuthRepository {
    suspend fun exchangeCodeForToken(
        code: String,
        clientId: String,
        clientSecret: String
    ): UserAccessTokenDto

    suspend fun refreshAccessToken(
        refreshToken: String,
        clientId: String,
        clientSecret: String
    ): UserAccessTokenDto
}
