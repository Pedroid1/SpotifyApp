package com.pedroid.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.DataResource
import com.pedroid.domain.repository.ArtistsRepository
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val artists = artistsRepository.getArtists()
        .cachedIn(viewModelScope)

    val state: StateFlow<HomeState> = flow {
        emit(HomeState(isLoading = true))
        when (val result = getUserProfileUseCase.getUserProfile()) {
            is DataResource.Success -> {
                emit(
                    HomeState(
                        isLoading = false,
                        userPhotoUrl = result.data.imageUrl,
                        userName = result.data.displayName
                    )
                )
            }

            is DataResource.Error -> {
                emit(
                    HomeState(
                        isLoading = false,
                        loadImageError = true
                    )
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())
}
