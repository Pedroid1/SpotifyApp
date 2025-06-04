package com.pedroid.data.local.artists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_remote_keys")
data class ArtistRemoteKeys(
    @PrimaryKey val artistId: String,
    val prevKey: Int?,
    val nextKey: Int?
)