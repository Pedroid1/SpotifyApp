package com.pedroid.data.repository.auth

import com.pedroid.data.remote.auth.dto.UserAccessTokenDto

interface AuthRepository {
    suspend fun exchangeCodeForToken(code: String): UserAccessTokenDto
    suspend fun refreshAccessToken(refreshToken: String): UserAccessTokenDto
}
