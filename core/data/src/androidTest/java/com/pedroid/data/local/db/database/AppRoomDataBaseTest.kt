package com.pedroid.data.local.db.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pedroid.data.local.db.AppRoomDataBase
import com.pedroid.data.local.db.album.AlbumDao
import com.pedroid.data.local.db.album.AlbumRemoteKeysDao
import com.pedroid.data.local.db.artists.ArtistsDao
import com.pedroid.data.local.db.artists.ArtistsRemoteKeysDao
import com.pedroid.data.local.db.playlists.PlaylistDao
import com.pedroid.data.local.db.playlists.PlaylistRemoteKeysDao
import com.pedroid.data.local.db.profile.ProfileDao
import org.junit.After
import org.junit.Before

internal abstract class AppRoomDataBaseTest {

    private lateinit var db: AppRoomDataBase

    protected lateinit var artistsDao: ArtistsDao
    protected lateinit var artistsRemoteKeysDao: ArtistsRemoteKeysDao

    protected lateinit var playlistDao: PlaylistDao
    protected lateinit var playlistRemoteKeysDao: PlaylistRemoteKeysDao

    protected lateinit var albumsDao: AlbumDao
    protected lateinit var albumsRemoteKeysDao: AlbumRemoteKeysDao

    protected lateinit var userProfileDao: ProfileDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                AppRoomDataBase::class.java
            ).build()
        }
        artistsDao = db.artistDao()
        artistsRemoteKeysDao = db.artistRemoteKeysDao()
        playlistDao = db.playlistDao()
        playlistRemoteKeysDao = db.playlistRemoteKeysDao()
        albumsDao = db.albumsDao()
        albumsRemoteKeysDao = db.albumsRemoteKeysDao()
        userProfileDao = db.userProfileDao()
    }

    @After
    fun tearDown() {
        db.close()
    }
}