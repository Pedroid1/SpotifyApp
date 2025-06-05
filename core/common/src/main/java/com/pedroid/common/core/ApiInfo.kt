package com.pedroid.common.core

object ApiInfo {
    const val CLIENT_ID = "47d05013895a49dba0bc1c89b226e130"
    const val CLIENT_SECRET = "1f947b7ba6834f4d942c1ebd1c6d68ae"
    const val REDIRECT_URI = "pedroid://callback"
    val SCOPES = arrayOf(
        "user-read-email",
        "user-top-read",
        "playlist-read-private",
        "playlist-modify-public",
        "playlist-modify-private"
    )
}
