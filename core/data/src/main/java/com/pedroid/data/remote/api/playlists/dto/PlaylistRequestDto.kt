package com.pedroid.data.remote.api.playlists.dto

class PlaylistRequestDto(
    val name: String,
    val description: String = "",
    val public: Boolean = false
)
