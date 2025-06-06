package com.pedroid.data.dao.user

import com.google.common.truth.Truth.assertThat
import com.pedroid.data.database.AppRoomDataBaseTest
import com.pedroid.data.local.db.profile.entity.UserProfileEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ProfileDaoTest : AppRoomDataBaseTest() {

    @Test
    fun saveInfoUser_and_getInfoUser_returnsCorrectData() = runTest {
        val user = testUserProfile(id = "1", name = "João")

        userProfileDao.saveInfoUser(user)

        val result = userProfileDao.getInfoUser()

        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo("1")
        assertThat(result?.name).isEqualTo("João")
    }

    @Test
    fun getInfoUser_returnsNull_whenNoUserSaved() = runTest {
        val result = userProfileDao.getInfoUser()

        assertThat(result).isNull()
    }

    private fun testUserProfile(
        id: String,
        name: String,
    ) = UserProfileEntity(
        id = id,
        name = name,
        imageUrl = "",
    )
}
