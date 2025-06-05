package com.pedroid.data.remote.profile

import com.pedroid.data.remote.profile.dto.UserProfileDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("v1/me")
    suspend fun getUserProfile(): UserProfileDto
}
