package com.pedroid.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pedroid.common.UiText
import com.pedroid.common.livedata.Event
import com.pedroid.common.loadDataResource
import com.pedroid.domain.usecase.artist.GetArtistsUseCase
import com.pedroid.domain.usecase.user.GetUserProfileUseCase
import com.pedroid.feature.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val artists = getArtistsUseCase.execute()
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _errorEvent = MutableLiveData<Event<UiText>>()
    val errorEvent get() = _errorEvent

    init {
        loadDataResource(
            fetch = { getUserProfileUseCase.getUserProfile() },
            onLoading = { isLoading ->
                _uiState.update { it.copy(isLoading = isLoading) }
            },
            onSuccess = { user -> _uiState.update { it.copy(userProfile = user) } },
            onError = { _errorEvent.postValue(Event(UiText.StringResource(R.string.error_fetching_data))) }
        )
    }
}
