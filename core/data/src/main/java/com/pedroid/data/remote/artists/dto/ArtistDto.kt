package com.pedroid.data.remote.artists.dto

import com.pedroid.data.remote.dto.ImageDto

data class ArtistDto(
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
)
