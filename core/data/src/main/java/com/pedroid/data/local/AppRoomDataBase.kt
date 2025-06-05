package com.pedroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pedroid.data.local.artists.ArtistsDao
import com.pedroid.data.local.artists.ArtistsRemoteKeysDao
import com.pedroid.data.local.artists.entity.ArtistEntity
import com.pedroid.data.local.artists.entity.ArtistRemoteKeys
import com.pedroid.data.local.profile.ProfileDao
import com.pedroid.data.local.profile.entity.UserProfileEntity

@Database(
    entities = [ArtistEntity::class, ArtistRemoteKeys::class,
               UserProfileEntity::class],
    version = 3
)
abstract class AppRoomDataBase : RoomDatabase() {
    abstract fun artistDao(): ArtistsDao
    abstract fun artistRemoteKeysDao(): ArtistsRemoteKeysDao
    abstract fun userProfileDao(): ProfileDao
}