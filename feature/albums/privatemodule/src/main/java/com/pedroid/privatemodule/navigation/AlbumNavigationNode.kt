package com.pedroid.privatemodule.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.AlbumNavigation
import com.pedroid.privatemodule.AlbumsFragment
import javax.inject.Inject

class AlbumNavigationNode @Inject constructor() : NavigationNode {

    override fun addNode(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            fragment<AlbumsFragment>(AlbumNavigation.Destination.Album.route)
        }
    }
}
