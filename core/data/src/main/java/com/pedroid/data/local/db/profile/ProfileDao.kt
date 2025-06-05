package com.pedroid.data.local.db.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedroid.data.local.db.profile.entity.UserProfileEntity

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInfoUser(userProfile: UserProfileEntity)

    @Query("SELECT * FROM user_profile")
    suspend fun getInfoUser(): UserProfileEntity?
}
