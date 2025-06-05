package com.pedroid.home

data class HomeState(
    val isLoading: Boolean = false,
    val userPhotoUrl: String? = null,
    val userName: String? = null,
    val loadImageError: Boolean = false
)
