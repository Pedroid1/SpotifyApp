package com.pedroid.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.core.UiText
import com.pedroid.common.extension.loadDataResourceFlow
import com.pedroid.common.livedata.Event
import com.pedroid.domain.usecase.artist.GetArtistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val artists = getArtistsUseCase.execute()
        .cachedIn(viewModelScope)

    val state: StateFlow<HomeUiState> = loadDataResourceFlow(
        initialState = HomeUiState(),
        fetch = { getUserProfileUseCase.getUserProfile() },
        onLoading = { copy(isLoading = it) },
        onSuccess = { copy(userProfile = it) },
        onError = {
            _errorEvent.value = Event(UiText.StringResource(R.string.error_fetching_data))
            this
        }
    )

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent
}
