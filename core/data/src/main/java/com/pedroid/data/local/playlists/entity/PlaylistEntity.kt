package com.pedroid.data.local.playlists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    val collaborative: Boolean,
    val description: String,
    @PrimaryKey val id: String,
    val imageUrl: String?,
    val name: String,
    val ownerName: String,
)