package com.pedroid.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pedroid.data.local.db.album.AlbumDao
import com.pedroid.data.local.db.album.AlbumRemoteKeysDao
import com.pedroid.data.local.db.album.entity.AlbumEntity
import com.pedroid.data.local.db.album.entity.AlbumRemoteKeys
import com.pedroid.data.local.db.artists.ArtistsDao
import com.pedroid.data.local.db.artists.ArtistsRemoteKeysDao
import com.pedroid.data.local.db.artists.entity.ArtistEntity
import com.pedroid.data.local.db.artists.entity.ArtistRemoteKeys
import com.pedroid.data.local.db.playlists.PlaylistDao
import com.pedroid.data.local.db.playlists.PlaylistRemoteKeysDao
import com.pedroid.data.local.db.playlists.entity.PlaylistEntity
import com.pedroid.data.local.db.playlists.entity.PlaylistRemoteKeys
import com.pedroid.data.local.db.profile.ProfileDao
import com.pedroid.data.local.db.profile.entity.UserProfileEntity

@Database(
    entities = [
        ArtistEntity::class, ArtistRemoteKeys::class,
        PlaylistEntity::class, PlaylistRemoteKeys::class,
        AlbumEntity::class, AlbumRemoteKeys::class,
        UserProfileEntity::class
    ],
    version = 10
)
abstract class AppRoomDataBase : RoomDatabase() {
    abstract fun artistDao(): ArtistsDao
    abstract fun artistRemoteKeysDao(): ArtistsRemoteKeysDao
    abstract fun userProfileDao(): ProfileDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistRemoteKeysDao(): PlaylistRemoteKeysDao
    abstract fun albumsDao(): AlbumDao
    abstract fun albumsRemoteKeysDao(): AlbumRemoteKeysDao
}
