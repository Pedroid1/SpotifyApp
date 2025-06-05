package com.pedroid.data.remote.artists.dto

import com.google.gson.annotations.SerializedName
import com.pedroid.data.remote.dto.ImageDto

data class AlbumDto(
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
    @SerializedName("release_date")
    val releaseDate: String,
)
