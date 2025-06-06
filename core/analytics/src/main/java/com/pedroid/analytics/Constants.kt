package com.pedroid.analytics

import com.google.firebase.analytics.FirebaseAnalytics

object Constants {

    // GENERAL
    const val SCREEN_VIEW = FirebaseAnalytics.Event.SCREEN_VIEW
    const val SELECT_ITEM = FirebaseAnalytics.Event.SELECT_ITEM
    const val FRAGMENT_NAME = "fragment_name"
    const val SELECT_ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
    const val SELECT_ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME

    // LOGOUT
    const val LOGOUT_EVENT = "logout"
    const val LOGOUT_METHOD = "logout_method"
    const val LOGOUT_MANUAL = "manual"

    // CREATE PLAYLIST
    const val CREATE_PLAYLIST_EVENT = "create_playlist"
    const val PLAYLIST_NAME = "playlist_name"
}
