package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val credentialServices: CredentialServices
) : ViewModel(){

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()


    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.onUsernameTextChange -> _uiState.update {
                it.copy(
                    email = event.username
                )
            }

            is LoginEvent.onPasswordTextChange -> _uiState.update {
                it.copy(
                    password = event.password
                )
            }
            LoginEvent.Login -> login()
            LoginEvent.Register -> TODO()
            LoginEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }
        }
    }

    private fun login() {
        if (_uiState.value.email.isEmpty() || _uiState.value.password.isEmpty()) {
            _uiState.update { it.copy(message = Constantes.camposVaciosMessage) }
        } else {
            viewModelScope.launch {
                credentialServices.login(
                    CredentialRequest(
                        _uiState.value.email,
                        _uiState.value.password
                    )
                ).catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            message = cause.message,
                            isLoading = false
                        )
                    }
                }).collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(
                                message = result.message,
                                isLoading = false
                            )
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }

                        is NetworkResultt.Success -> _uiState.update {
                            it.copy(
                                logged = true,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }

    }

}