package com.pedroid.data.local.db.artists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroid.model.Artist

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val imageUrl: String?,
    val name: String,
) {
    fun toDomain(): Artist {
        return Artist(
            id = id,
            imageUrl = imageUrl,
            name = name
        )
    }
}
