package com.pedroid.publicmodule

import com.pedroid.model.Artist
import java.io.Serializable

interface AlbumsFeatureCommunicator {

    companion object {
        const val albumsFeatureNavKey = "registerFeatureNavKey"
    }

    fun launchFeature(albumsFeatureArgs: AlbumsFeatureArgs)

    data class AlbumsFeatureArgs(
        val previousRoute: String,
        val artist: Artist
    ): Serializable
}