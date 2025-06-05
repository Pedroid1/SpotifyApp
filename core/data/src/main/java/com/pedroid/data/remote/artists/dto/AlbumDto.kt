package com.pedroid.data.remote.artists.dto

import com.google.gson.annotations.SerializedName
import com.pedroid.data.dto.ExternalUrlsDto
import com.pedroid.data.dto.ImageDto

data class AlbumDto(
    @SerializedName("album_group")
    val albumGroup: String,
    @SerializedName("album_type")
    val albumType: String,
    val artists: List<ArtistAlbumDto>,
    @SerializedName("available_markets")
    val availableMarkets: List<String>,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>?,
    val name: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String,
    val restrictions: RestrictionsDto,
    @SerializedName("total_tracks")
    val totalTracks: Int,
    val type: String,
    val uri: String
)
