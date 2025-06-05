package com.pedroid.privatemodule.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.pedroid.navigation.features.AlbumNavigation
import com.pedroid.navigation.navigateEnterRightAnimation
import com.pedroid.publicmodule.AlbumsFeatureCommunicator
import javax.inject.Inject

class AlbumsFeatureCommunicatorImpl @Inject constructor(private val navController: NavController) :
    AlbumsFeatureCommunicator {

    companion object {
        const val ARTIST_KEY = "artist"
    }

    override fun launchFeature(albumsFeatureArgs: AlbumsFeatureCommunicator.AlbumsFeatureArgs) {
        navController.navigateEnterRightAnimation(
            route = AlbumNavigation.Destination.Album.route,
            args = bundleOf(ARTIST_KEY to albumsFeatureArgs.artist)
        )
    }
}