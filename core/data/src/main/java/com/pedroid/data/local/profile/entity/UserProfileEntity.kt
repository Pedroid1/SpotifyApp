package com.pedroid.data.local.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroid.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String?,
) {
    fun toDomain(): UserProfile {
        return UserProfile(id, name, imageUrl)
    }
}
