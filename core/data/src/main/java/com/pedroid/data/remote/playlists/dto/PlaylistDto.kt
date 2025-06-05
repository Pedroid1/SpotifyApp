package com.pedroid.data.remote.playlists.dto


import com.google.gson.annotations.SerializedName
import com.pedroid.data.dto.ExternalUrlsDto
import com.pedroid.data.dto.ImageDto

data class PlaylistDto(
    val collaborative: Boolean,
    val description: String,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val owner: OwnerDto,
    @SerializedName("primary_color")
    val primaryColor: Any,
    val `public`: Boolean,
)