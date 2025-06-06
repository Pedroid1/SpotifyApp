package com.pedroid.domain.usecase.user

import com.pedroid.common.core.DataResource
import com.pedroid.model.UserProfile

interface GetUserProfileUseCase {
    suspend fun getUserProfile(): DataResource<UserProfile>
}