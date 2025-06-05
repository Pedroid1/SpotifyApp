package com.pedroid.domain.usecase.user

import com.pedroid.common.DataResource
import com.pedroid.domain.repository.ProfileRepository
import com.pedroid.model.UserProfile
import javax.inject.Inject

class GetUserProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : GetUserProfileUseCase {

    override suspend fun getUserProfile(): DataResource<UserProfile> {
        return when (val remoteResult = profileRepository.getUserProfile()) {
            is DataResource.Success -> {
                profileRepository.saveInfoUser(remoteResult.data)
                remoteResult
            }

            is DataResource.Error -> {
                val local = profileRepository.getInfoUser()
                local?.let { DataResource.Success(it) }
                    ?: DataResource.Error(remoteResult.exception)
            }
        }
    }
}
