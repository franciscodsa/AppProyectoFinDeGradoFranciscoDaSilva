package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel(){

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()


    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.onUsernameTextChange -> _uiState.update {
                it.copy(
                    username = event.username
                )
            }

            is LoginEvent.onPasswordTextChange -> _uiState.update {
                it.copy(
                    password = event.password
                )
            }
            LoginEvent.Login -> TODO()
            LoginEvent.Register -> TODO()
            LoginEvent.MessageSeen -> TODO()
        }
    }
}