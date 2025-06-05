package com.pedroid.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.pedroid.home.HomeFragment
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.HomeNavigation
import javax.inject.Inject

class HomeNavigationNode @Inject constructor() : NavigationNode {

    override fun addNode(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            fragment<HomeFragment>(HomeNavigation.Destination.Home.route)
        }
    }
}
