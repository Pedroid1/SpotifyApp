package com.pedroid.data.local.db.playlists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_remote_keys")
data class PlaylistRemoteKeys(
    @PrimaryKey val playlistId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
