package com.pedroid.data.local.db.artists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedroid.data.local.db.artists.entity.ArtistRemoteKeys

@Dao
interface ArtistsRemoteKeysDao {

    @Query("SELECT * FROM artist_remote_keys WHERE artistId = :id")
    suspend fun remoteKeysById(id: String): ArtistRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<ArtistRemoteKeys>)

    @Query("DELETE FROM artist_remote_keys")
    suspend fun clearRemoteKeys()
}
