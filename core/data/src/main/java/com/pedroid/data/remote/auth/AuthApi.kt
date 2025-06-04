package com.pedroid.data.remote.auth

import com.pedroid.common.ApiInfo
import com.pedroid.data.remote.auth.dto.UserAccessTokenDto
import okhttp3.Credentials
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("api/token")
    suspend fun getAccessToken(
        @Header("Authorization") auth: String = Credentials.basic(
            ApiInfo.CLIENT_ID,
            ApiInfo.CLIENT_SECRET
        ),
        @Header("Content-Type") content: String = "application/x-www-form-urlencoded",
        @Field("code") code: String?,
        @Field("redirect_uri") redirectUri: String = ApiInfo.REDIRECT_URI,
        @Field("grant_type") grantType: String = "authorization_code"
    ): UserAccessTokenDto

    @FormUrlEncoded
    @POST("api/token")
    suspend fun getNewToken(
        @Header("Authorization") auth: String = Credentials.basic(
            ApiInfo.CLIENT_ID,
            ApiInfo.CLIENT_SECRET
        ),
        @Header("Content-Type") content: String = "application/x-www-form-urlencoded",
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = ApiInfo.CLIENT_ID
    ): UserAccessTokenDto
}
