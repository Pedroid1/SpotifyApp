package com.pedroid.data.remote.api.profile.dto

import com.google.gson.annotations.SerializedName
import com.pedroid.data.remote.api.dto.ImageDto
import com.pedroid.model.UserProfile

data class UserProfileDto(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    val email: String,
    val images: List<ImageDto>
) {
    fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            displayName = displayName,
            imageUrl = images.firstOrNull()?.url
        )
    }
}
