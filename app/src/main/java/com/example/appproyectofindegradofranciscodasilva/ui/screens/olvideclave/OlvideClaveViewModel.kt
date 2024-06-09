package com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialService
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
class OlvideClaveViewModel @Inject constructor(
    private val credentialService: CredentialService
) : ViewModel() {

    private val _uiState = MutableStateFlow(OlvideClaveState())
    val uiState: StateFlow<OlvideClaveState> = _uiState.asStateFlow()

    fun handleEvent(event: OlvideClaveEvent) {
        when (event) {
            is OlvideClaveEvent.OnEmailChange -> _uiState.update {
                it.copy(email = event.email)
            }

            is OlvideClaveEvent.OnNewPasswordChange -> _uiState.update {
                it.copy(newPassword = event.newPassword)
            }

            is OlvideClaveEvent.OnConfirmPasswordChange -> _uiState.update {
                it.copy(confirmPassword = event.confirmPassword)
            }

            is OlvideClaveEvent.OnAuthCodeChange -> _uiState.update {
                it.copy(authCode = event.authCode)
            }

            OlvideClaveEvent.OnSendEmail -> sendEmail()
            OlvideClaveEvent.OnChangePassword -> changePassword()

            OlvideClaveEvent.MessageSeen -> _uiState.update {
                it.copy(message = null)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendEmail() {
        val email = _uiState.value.email
        if (email.isEmpty()) {
            _uiState.update { it.copy(message = Constantes.camposVaciosMessage) }
        } else if (!isValidEmail(email)) {
            _uiState.update { it.copy(message = "Email inválido") }
        } else {
            viewModelScope.launch {
                credentialService.getAuthCode(email)
                    .catch { cause ->
                        _uiState.update {
                            it.copy(
                                message = cause.message,
                                isLoading = false
                            )
                        }
                    }
                    .collect { result ->
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
                                    message = result.data?.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun changePassword() {
        val state = _uiState.value
        if (state.newPassword.isEmpty() || state.confirmPassword.isEmpty() || state.authCode.isEmpty()) {
            _uiState.update { it.copy(message = Constantes.camposVaciosMessage) }
        }else {
            viewModelScope.launch {
                credentialService.changePassword(
                    state.newPassword,
                    state.confirmPassword,
                    state.authCode
                ).catch { cause ->
                    _uiState.update {
                        it.copy(
                            message = cause.message,
                            isLoading = false
                        )
                    }
                }.collect { result ->
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
                                message = result.data?.message,
                                isLoading = false,
                                newPassword = "",
                                confirmPassword = "",
                                authCode = ""
                            )
                        }
                    }
                }
            }
        }
    }
}