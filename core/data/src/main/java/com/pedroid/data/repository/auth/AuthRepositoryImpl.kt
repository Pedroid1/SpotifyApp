package com.pedroid.data.repository.auth

import com.pedroid.common.dispatcher.BinDispatchers
import com.pedroid.common.dispatcher.Dispatcher
import com.pedroid.data.remote.api.auth.AuthApi
import com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @Dispatcher(BinDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun exchangeCodeForToken(code: String): UserAccessTokenDto {
        return withContext(ioDispatcher) {
            authApi.getAccessToken(code = code)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): UserAccessTokenDto {
        return withContext(ioDispatcher) {
            authApi.getNewToken(refreshToken = refreshToken)
        }
    }
}
