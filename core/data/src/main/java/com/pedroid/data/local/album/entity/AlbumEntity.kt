package com.pedroid.data.local.album.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Album"
)
data class AlbumEntity(
    @PrimaryKey val id: String,
    val artistId: String,
    val imageUrl: String,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
)