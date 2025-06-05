package com.pedroid.profile

import com.pedroid.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
)
