package com.pedroid.data.remote.api.auth.dto

import com.google.gson.annotations.SerializedName

data class UserAccessTokenDto(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val tokenRefresh: String?
)
