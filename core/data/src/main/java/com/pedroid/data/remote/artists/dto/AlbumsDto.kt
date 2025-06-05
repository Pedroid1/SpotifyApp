package com.pedroid.data.remote.artists.dto

data class AlbumsDto(
    val href: String,
    val items: List<AlbumDto>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)
