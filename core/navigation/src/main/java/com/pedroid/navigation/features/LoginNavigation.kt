package com.pedroid.navigation.features

object LoginNavigation {

    const val ROUTE = "login_route"
    private const val START_DESTINATION = ROUTE

    sealed class Destination(val route: String) {
        data object Login : Destination(START_DESTINATION)
    }
}