package com.pedroid.data.local.db.playlists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroid.model.Playlist

@Entity(tableName = "playlist")
data class PlaylistEntity(
    val description: String,
    @PrimaryKey val id: String,
    val imageUrl: String?,
    val name: String,
) {
    fun toDomain(): Playlist {
        return Playlist(
            description = description,
            id = id,
            imageUrl = imageUrl,
            name = name,
        )
    }
}
