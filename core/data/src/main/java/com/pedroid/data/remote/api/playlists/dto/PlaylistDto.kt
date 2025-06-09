package com.pedroid.data.remote.api.playlists.dto

import com.pedroid.data.remote.api.dto.ImageDto

data class PlaylistDto(
    val description: String?,
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
)
