package com.pedroid.data.repository.profile

import com.pedroid.common.core.DataResource
import com.pedroid.data.dispatcher.BinDispatchers
import com.pedroid.data.dispatcher.Dispatcher
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.profile.entity.UserProfileEntity
import com.pedroid.data.remote.api.profile.ProfileApi
import com.pedroid.domain.repository.ProfileRepository
import com.pedroid.model.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @Dispatcher(BinDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val api: ProfileApi,
    private val database: AppRoomDataBase
) : ProfileRepository {

    override suspend fun getUserProfile(): DataResource<UserProfile> {
        return withContext(ioDispatcher) {
            try {
                val remoteUser = api.getUserProfile().toDomain()
                saveInfoUser(remoteUser)
                DataResource.Success(remoteUser)
            } catch (e: HttpException) {
                handleCacheFallback(e)
            } catch (e: IOException) {
                handleCacheFallback(e)
            }
        }
    }

    private suspend fun handleCacheFallback(exception: Exception): DataResource<UserProfile> {
        return withContext(ioDispatcher) {
            val cached = getInfoUser()
            if (cached != null) {
                DataResource.Success(cached)
            } else {
                DataResource.Error(exception)
            }
        }
    }

    private suspend fun saveInfoUser(userProfile: UserProfile) {
        withContext(ioDispatcher) {
            database.userProfileDao().saveInfoUser(
                UserProfileEntity(
                    id = userProfile.id,
                    name = userProfile.displayName,
                    imageUrl = userProfile.imageUrl
                )
            )
        }
    }

    private suspend fun getInfoUser(): UserProfile? {
        return withContext(ioDispatcher) {
            database.userProfileDao().getInfoUser()?.toDomain()
        }
    }
}
