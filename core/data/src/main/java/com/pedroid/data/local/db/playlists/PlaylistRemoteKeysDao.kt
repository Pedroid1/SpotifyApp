package com.pedroid.data.local.db.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedroid.data.local.db.playlists.entity.PlaylistRemoteKeys

@Dao
interface PlaylistRemoteKeysDao {

    @Query("SELECT * FROM playlist_remote_keys WHERE playlistId = :id")
    suspend fun remoteKeysById(id: String): PlaylistRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<PlaylistRemoteKeys>)

    @Query("DELETE FROM playlist_remote_keys")
    suspend fun clearRemoteKeys()
}
