package com.pedroid.data.repository

import com.pedroid.common.DataResource
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

    override suspend fun getUserProfile(): DataResource<UserProfile> = try {
        DataResource.Success(api.getUserProfile().toDomain())
    } catch (e: HttpException) {
        DataResource.Error(e)
    } catch (e: IOException) {
        DataResource.Error(e)
    }

    override suspend fun saveInfoUser(userProfile: UserProfile) {
        database.userProfileDao().saveInfoUser(
            UserProfileEntity(
                name = userProfile.displayName,
                imageUrl = userProfile.imageUrl
            )
        )
    }

    override suspend fun getInfoUser(): UserProfile? = database.userProfileDao().getInfoUser()?.toDomain()
}
