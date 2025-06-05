package com.pedroid.data.repository.auth

import com.pedroid.data.dispatcher.BinDispatchers
import com.pedroid.data.dispatcher.Dispatcher
import com.pedroid.data.remote.api.auth.AuthApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @Dispatcher(BinDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun exchangeCodeForToken(code: String): com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto {
        return withContext(ioDispatcher) {
            authApi.getAccessToken(code = code)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): com.pedroid.data.remote.api.auth.dto.UserAccessTokenDto {
        return withContext(ioDispatcher) {
            authApi.getNewToken(refreshToken = refreshToken)
        }
    }
}
