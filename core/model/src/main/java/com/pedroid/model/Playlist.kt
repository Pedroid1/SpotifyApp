package com.pedroid.model

data class Playlist(
    val collaborative: Boolean,
    val description: String,
    val id: String,
    val imageUrl: String,
    val name: String,
    val ownerName: String,
)