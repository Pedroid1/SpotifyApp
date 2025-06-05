package com.pedroid.navigation.features

object PlaylistNavigation {

    const val ROUTE = "playlist_route"
    private const val START_DESTINATION = ROUTE

    sealed class Destination(val route: String) {
        data object Playlist : Destination(START_DESTINATION)
    }
}