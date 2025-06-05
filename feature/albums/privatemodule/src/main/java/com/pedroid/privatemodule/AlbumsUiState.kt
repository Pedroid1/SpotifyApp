package com.pedroid.privatemodule

import com.pedroid.model.Artist

data class AlbumsUiState(
    val isLoading: Boolean = false,
    val artist: Artist? = null,
)
