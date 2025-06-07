package com.pedroid.data.repository.profile

import com.google.common.truth.Truth.assertThat
import com.pedroid.common.core.DataResource
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.profile.ProfileDao
import com.pedroid.data.local.db.profile.entity.UserProfileEntity
import com.pedroid.data.remote.api.dto.ImageDto
import com.pedroid.data.remote.api.profile.ProfileApi
import com.pedroid.data.remote.api.profile.dto.UserProfileDto
import com.pedroid.model.UserProfile
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class ProfileRepositoryImplTest {

    private lateinit var api: ProfileApi
    private lateinit var dao: ProfileDao
    private lateinit var db: AppRoomDataBase
    private lateinit var repository: ProfileRepositoryImpl

    private val fakeRemoteProfile = UserProfileDto(
        id = "123",
        displayName = "Ana",
        listOf(
            ImageDto(url = "https://img.url")
        )
    )

    private val fakeDomainProfile = UserProfile(
        id = "123",
        displayName = "Ana",
        imageUrl = "https://img.url"
    )

    private val cachedEntity = UserProfileEntity(
        id = "123",
        name = "Ana",
        imageUrl = "https://img.url"
    )

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk(relaxed = true)
        db = mockk()

        every { db.userProfileDao() } returns dao

        repository = ProfileRepositoryImpl(
            ioDispatcher = Dispatchers.Unconfined,
            api = api,
            database = db
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getUserProfile should return remote profile and cache it`() = runTest {
        coEvery { api.getUserProfile() } returns fakeRemoteProfile

        val result = repository.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
        assertThat((result as DataResource.Success).data).isEqualTo(fakeDomainProfile)

        coVerify { api.getUserProfile() }
        coVerify {
            dao.saveInfoUser(
                UserProfileEntity(
                    fakeRemoteProfile.id,
                    fakeRemoteProfile.displayName,
                    fakeRemoteProfile.images?.firstOrNull()?.url
                )
            )
        }
    }

    @Test
    fun `getUserProfile should fallback to cache on HttpException`() = runTest {
        coEvery { api.getUserProfile() } throws mockk<HttpException>()
        coEvery { dao.getInfoUser() } returns cachedEntity

        val result = repository.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
        assertThat((result as DataResource.Success).data).isEqualTo(cachedEntity.toDomain())
    }

    @Test
    fun `getUserProfile should fallback to cache on IOException`() = runTest {
        coEvery { api.getUserProfile() } throws IOException()
        coEvery { dao.getInfoUser() } returns cachedEntity

        val result = repository.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Success::class.java)
        assertThat((result as DataResource.Success).data).isEqualTo(cachedEntity.toDomain())
    }

    @Test
    fun `getUserProfile returns Error if remote fails and no cache exists`() = runTest {
        coEvery { api.getUserProfile() } throws IOException()
        coEvery { dao.getInfoUser() } returns null

        val result = repository.getUserProfile()

        assertThat(result).isInstanceOf(DataResource.Error::class.java)
    }
}
