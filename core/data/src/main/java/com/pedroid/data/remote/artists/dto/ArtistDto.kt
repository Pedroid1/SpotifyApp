package com.pedroid.data.remote.artists.dto

import com.google.gson.annotations.SerializedName
import com.pedroid.data.dto.ExternalUrlsDto
import com.pedroid.data.dto.FollowersDto
import com.pedroid.data.dto.ImageDto

data class ArtistDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto,
    val followers: FollowersDto,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)
