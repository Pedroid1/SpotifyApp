package com.pedroid.data.remote.playlists.dto


import com.google.gson.annotations.SerializedName
import com.pedroid.data.dto.ExternalUrlsDto

data class OwnerDto(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)