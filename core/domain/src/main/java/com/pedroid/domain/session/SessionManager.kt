package com.pedroid.domain.session

interface SessionManager {
    suspend fun loginWithCode(code: String, clientId: String, clientSecret: String): Result<Unit>
    suspend fun refreshAccessToken(): Boolean
    fun getAccessToken(): String?
    fun isLoggedIn(): Boolean
    fun clearSession()
    suspend fun ensureValidSession(): Boolean
}
