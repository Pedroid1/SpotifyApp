package com.pedroid.common.core

object ApiInfo {
    const val REDIRECT_URI = "pedroid://callback"
    val SCOPES = arrayOf(
        "user-read-email",
        "user-top-read",
        "playlist-read-private",
        "playlist-modify-public",
        "playlist-modify-private"
    )
}
