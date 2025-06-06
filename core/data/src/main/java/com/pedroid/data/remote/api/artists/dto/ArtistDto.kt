package com.pedroid.data.remote.api.artists.dto

import com.pedroid.data.remote.api.dto.ImageDto

data class ArtistDto(
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
)
