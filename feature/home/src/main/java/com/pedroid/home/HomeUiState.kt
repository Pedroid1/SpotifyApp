package com.pedroid.home

import com.pedroid.model.UserProfile

data class HomeUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
)
