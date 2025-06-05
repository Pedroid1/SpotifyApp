package com.pedroid.data.local.artists

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pedroid.data.local.artists.entity.ArtistEntity

@Dao
interface ArtistsDao {

    @Upsert
    suspend fun upsertArtists(artists: List<ArtistEntity>)

    @Query("SELECT * FROM artist")
    fun pagingSource(): PagingSource<Int, ArtistEntity>

    @Query("DELETE FROM artist")
    suspend fun clearAll()
}
