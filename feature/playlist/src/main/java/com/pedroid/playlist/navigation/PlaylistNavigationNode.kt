package com.pedroid.playlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.PlaylistNavigation
import com.pedroid.playlist.PlaylistFragment
import javax.inject.Inject

class PlaylistNavigationNode @Inject constructor() : NavigationNode {

    override fun addNode(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            fragment<PlaylistFragment>(PlaylistNavigation.Destination.Playlist.route)
        }
    }
}
