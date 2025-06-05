package com.pedroid.data.local.album.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroid.model.Album

@Entity(
    tableName = "album"
)
data class AlbumEntity(
    @PrimaryKey val id: String,
    val artistId: String?,
    val name: String,
    val imageUrl: String?,
    val releaseDate: String
) {
    fun toDomain(): Album {
        return Album(
            id = id,
            name = name,
            imageUrl = imageUrl,
            releaseDate = releaseDate
        )
    }
}
