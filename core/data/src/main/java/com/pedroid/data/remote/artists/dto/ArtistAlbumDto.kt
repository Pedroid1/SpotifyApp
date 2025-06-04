package com.pedroid.data.remote.artists.dto

import com.google.gson.annotations.SerializedName
import com.pedroid.data.dto.ExternalUrlsDto

data class ArtistAlbumDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)