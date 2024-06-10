package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.domain.services.AccountantService
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialService
import com.example.appproyectofindegradofranciscodasilva.domain.services.UserService
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
class AccountantViewModel @Inject constructor(
    private val accountantService: AccountantService,
    private val userService: UserService,
    private val credentialService: CredentialService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountantState())
    val uiState: StateFlow<AccountantState> = _uiState.asStateFlow()

    init {
        loadAccountants()
    }

    fun handleEvent(event: AccountantEvent) {
        when (event) {
            is AccountantEvent.OnAccountantExpandChanged -> _uiState.update {
                it.copy(expandedAccountantId = if (it.expandedAccountantId == event.email) null else event.email)
            }

            AccountantEvent.LoadAccountants -> loadAccountants()
            is AccountantEvent.DeleteAccountant -> deleteAccountant(event.email)

            AccountantEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }

            AccountantEvent.SetUserRole -> setRole()
        }
    }


    private fun setRole() {
        viewModelScope.launch {
            val role = credentialService.getRole()

            _uiState.update {
                it.copy(
                    userRole = role
                )
            }

        }
    }

    private fun deleteAccountant(email: String) {
        viewModelScope.launch {
            userService.deleteUser(email)
                .catch { cause ->
                    _uiState.update {
                        it.copy(message = cause.message, isLoading = false)
                    }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> {

                            credentialService.deleteCredentials(email)
                                .catch { cause ->
                                    _uiState.update {
                                        it.copy(message = cause.message, isLoading = false)
                                    }
                                }
                                .collect { deleteResult ->
                                    when (deleteResult) {
                                        is NetworkResultt.Success -> {
                                            _uiState.update {
                                                it.copy(message = deleteResult.data?.message)
                                            }
                                            loadAccountants()
                                        }

                                        is NetworkResultt.Error -> _uiState.update {
                                            it.copy(
                                                message = deleteResult.message,
                                                isLoading = false
                                            )
                                        }

                                        is NetworkResultt.Loading -> _uiState.update {
                                            it.copy(
                                                isLoading = true
                                            )
                                        }
                                    }
                                }

                        }

                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(
                                message = result.data?.message,
                                isLoading = false
                            )
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }

    private fun loadAccountants() {
        viewModelScope.launch {
            accountantService.getAccountants()
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
                        is NetworkResultt.Success -> {
                            _uiState.update {
                                it.copy(
                                    accountants = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                        }

                        is NetworkResultt.Error -> {
                            _uiState.update {
                                it.copy(message = result.message, isLoading = false)
                            }
                        }

                        is NetworkResultt.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }
}
