package com.pedroid.domain.repository

import com.pedroid.common.DataResource
import com.pedroid.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): DataResource<UserProfile>
}