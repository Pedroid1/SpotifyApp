package com.pedroid.playlist

import com.pedroid.model.UserProfile

data class PlaylistUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
)
