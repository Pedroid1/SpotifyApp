package com.pedroid.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.domain.repository.ArtistsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository
) : ViewModel() {

    val artists = artistsRepository.getArtists()
        .cachedIn(viewModelScope)


}