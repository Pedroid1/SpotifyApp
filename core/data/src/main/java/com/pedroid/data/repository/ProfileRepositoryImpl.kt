package com.pedroid.data.repository

import com.pedroid.common.core.DataResource
import com.pedroid.data.local.AppRoomDataBase
import com.pedroid.data.local.profile.entity.UserProfileEntity
import com.pedroid.data.remote.profile.ProfileApi
import com.pedroid.domain.repository.ProfileRepository
import com.pedroid.model.UserProfile
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val database: AppRoomDataBase
) : ProfileRepository {

    override suspend fun getUserProfile(): DataResource<UserProfile> {
        return try {
            val remoteUser = api.getUserProfile().toDomain()
            saveInfoUser(remoteUser)
            DataResource.Success(remoteUser)
        } catch (e: HttpException) {
            handleCacheFallback(e)
        } catch (e: IOException) {
            handleCacheFallback(e)
        }
    }

    private suspend fun handleCacheFallback(exception: Exception): DataResource<UserProfile> {
        val cached = getInfoUser()
        return if (cached != null) {
            DataResource.Success(cached)
        } else {
            DataResource.Error(exception)
        }
    }

    private suspend fun saveInfoUser(userProfile: UserProfile) {
        database.userProfileDao().saveInfoUser(
            UserProfileEntity(
                id = userProfile.id,
                name = userProfile.displayName,
                imageUrl = userProfile.imageUrl
            )
        )
    }

    private suspend fun getInfoUser(): UserProfile? {
        return database.userProfileDao().getInfoUser()?.toDomain()
    }
}
