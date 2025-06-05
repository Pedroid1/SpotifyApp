package com.pedroid.domain.usecase.user

import com.pedroid.common.DataResource
import com.pedroid.domain.repository.ProfileRepository
import com.pedroid.model.UserProfile
import javax.inject.Inject

class GetUserProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : GetUserProfileUseCase {

    override suspend fun getUserProfile(): DataResource<UserProfile> = profileRepository.getUserProfile()
}
