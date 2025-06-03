package com.pedroid.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.pedroid.login.LoginFragment
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.LoginNavigation
import javax.inject.Inject

class LoginNavigationNode @Inject constructor() : NavigationNode {

    override fun addNode(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            fragment<LoginFragment>(LoginNavigation.Destination.Login.route)
        }
    }
}