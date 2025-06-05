package com.pedroid.data.remote.playlists.dto


data class PlaylistResponseDto(
    val href: String,
    val items: List<PlaylistDto>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
)