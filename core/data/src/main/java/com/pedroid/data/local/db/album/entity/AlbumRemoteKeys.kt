package com.pedroid.data.local.db.album.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_remote_keys")
data class AlbumRemoteKeys(
    @PrimaryKey val albumId: String,
    val artistId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
