package com.pedroid.data.local.album

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedroid.data.local.album.entity.AlbumRemoteKeys

@Dao
interface AlbumRemoteKeysDao {

    @Query("SELECT * FROM album_remote_keys WHERE albumId = :id")
    suspend fun remoteKeysById(id: String): AlbumRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<AlbumRemoteKeys>)

    @Query("DELETE FROM album_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("DELETE FROM album_remote_keys WHERE artistId = :artistId")
    suspend fun clearRemoteKeysByArtistId(artistId: String)
}
