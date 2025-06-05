package com.pedroid.data.local.album

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pedroid.data.local.album.entity.AlbumEntity

@Dao
interface AlbumDao {

    @Upsert
    suspend fun upsertAlbums(artists: List<AlbumEntity>)

    @Query("SELECT * FROM album where artistId = :artistId")
    fun pagingSource(artistId: String): PagingSource<Int, AlbumEntity>

    @Query("DELETE FROM album WHERE artistId = :artistId")
    suspend fun clearByArtistId(artistId: String)

    @Query("DELETE FROM album")
    suspend fun clearAll()
}
