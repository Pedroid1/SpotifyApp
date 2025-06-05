package com.pedroid.navigation.features

object HomeNavigation {

    const val ROUTE = "home_route"
    private const val START_DESTINATION = ROUTE

    sealed class Destination(val route: String) {
        data object Home : Destination(START_DESTINATION)
    }
}