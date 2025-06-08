package com.pedroid.navigation.features

object ProfileNavigation {

    const val ROUTE = "profile_route"
    private const val START_DESTINATION = ROUTE

    sealed class Destination(val route: String) {
        data object Profile : Destination(START_DESTINATION)
    }
}
