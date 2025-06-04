package com.pedroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pedroid.data.local.artists.ArtistsDao
import com.pedroid.data.local.artists.ArtistsRemoteKeysDao
import com.pedroid.data.local.artists.entity.ArtistEntity
import com.pedroid.data.local.artists.entity.ArtistRemoteKeys

@Database(
    entities = [ArtistEntity::class, ArtistRemoteKeys::class],
    version = 1
)
abstract class AppRoomDataBase : RoomDatabase() {
    abstract fun artistDao(): ArtistsDao
    abstract fun artistRemoteKeys(): ArtistsRemoteKeysDao
}