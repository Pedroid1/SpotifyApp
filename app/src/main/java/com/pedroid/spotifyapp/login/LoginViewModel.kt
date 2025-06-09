package com.pedroid.spotifyapp.login

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.common.core.ApiInfo
import com.pedroid.common.core.UiText
import com.pedroid.common.livedata.Event
import com.pedroid.domain.session.SessionManager
import com.pedroid.spotifyapp.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _launchAuthenticationLiveData = MutableLiveData<Event<AuthorizationRequest>>()
    val launchAuthenticationLiveData get() = _launchAuthenticationLiveData

    private val _errorLiveData = MutableLiveData<Event<UiText>>()
    val errorLiveData get() = _errorLiveData

    private val _loginSuccessLiveData = MutableLiveData<Event<Unit>>()
    val loginSuccessLiveData get() = _loginSuccessLiveData

    fun loginButtonClick() {
        val builder =
            AuthorizationRequest.Builder(
                BuildConfig.CLIENT_ID,
                AuthorizationResponse.Type.CODE,
                ApiInfo.REDIRECT_URI
            ).setScopes(ApiInfo.SCOPES)
                .setShowDialog(!sessionManager.isLoggedIn())
        _launchAuthenticationLiveData.value = Event(builder.build())
    }

    fun handleAuthResponse(data: Intent?, resultCode: Int) {
        val authResponse = AuthorizationClient.getResponse(resultCode, data)
        when (authResponse.type) {
            AuthorizationResponse.Type.CODE -> {
                val code = authResponse.code
                viewModelScope.launch {
                    val result = sessionManager.loginWithCode(code, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
                    if (result.isSuccess) {
                        _loginSuccessLiveData.value = Event(Unit)
                    } else {
                        _errorLiveData.value = Event(
                            UiText.DynamicString("Falha ao logar: ${result.exceptionOrNull()?.message}")
                        )
                    }
                }
            }

            AuthorizationResponse.Type.ERROR -> {
                errorLiveData.value = Event(UiText.DynamicString("Error Authentication Response"))
            }

            else -> Unit
        }
    }
}
