package com.pedroid.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.ProfileNavigation
import com.pedroid.profile.ProfileFragment
import javax.inject.Inject

class ProfileNavigationNode @Inject constructor() : NavigationNode {

    override fun addNode(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            fragment<ProfileFragment>(ProfileNavigation.Destination.Profile.route)
        }
    }
}
