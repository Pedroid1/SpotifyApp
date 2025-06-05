package com.pedroid.data.local.playlists

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pedroid.data.local.playlists.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun upsertPlaylists(playlists: List<PlaylistEntity>)

    @Query("SELECT * FROM playlist")
    fun pagingSource(): PagingSource<Int, PlaylistEntity>

    @Query("DELETE FROM playlist")
    suspend fun clearAll()
}
