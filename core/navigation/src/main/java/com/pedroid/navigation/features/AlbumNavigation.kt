package com.pedroid.navigation.features

object AlbumNavigation {

    const val ROUTE = "albums_route"
    private const val START_DESTINATION = ROUTE

    sealed class Destination(val route: String) {
        data object Album : Destination(START_DESTINATION)
    }
}