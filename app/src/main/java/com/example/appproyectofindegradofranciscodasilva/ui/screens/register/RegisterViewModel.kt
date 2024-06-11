package com.example.appproyectofindegradofranciscodasilva.ui.screens.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.domain.services.AccountantService
import com.example.appproyectofindegradofranciscodasilva.domain.services.ChatService
import com.example.appproyectofindegradofranciscodasilva.domain.services.ClientService
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialService
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val credentialService: CredentialService,
    private val clientService: ClientService,
    private val accountantService: AccountantService,
    private val chatService: ChatService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun handleEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }

            RegisterEvent.Register -> register()

            is RegisterEvent.OnEmailTextChange -> _uiState.update {
                it.copy(
                    email = event.email
                )
            }

            is RegisterEvent.OnPhoneTextChange -> _uiState.update {
                it.copy(
                    phone = event.phone
                )
            }

            is RegisterEvent.OnPasswordTextChange -> _uiState.update {
                it.copy(
                    password = event.password
                )
            }

            is RegisterEvent.OnPasswordConfirmTextChange -> _uiState.update {
                it.copy(
                    confirmPassword = event.confirmPassword
                )
            }


            is RegisterEvent.OnFirstNameTextChange -> _uiState.update {
                it.copy(
                    firstName = event.name
                )
            }

            is RegisterEvent.OnLastNameChange -> _uiState.update {
                it.copy(
                    lastNames = event.lastName
                )
            }

            is RegisterEvent.OnDayFieldChange -> _uiState.update {
                it.copy(
                    day = event.day
                )
            }

            is RegisterEvent.OnMonthFieldChange -> _uiState.update {
                it.copy(
                    month = event.month
                )
            }

            is RegisterEvent.OnYearFieldChange -> _uiState.update {
                it.copy(
                    year = event.year
                )
            }

            is RegisterEvent.SetUserRole -> setRole()
            is RegisterEvent.OnSelectedUserType -> _uiState.update { it.copy(selectedUserType = event.selectedUserType) }
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


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun register() {
        if (_uiState.value.email.isEmpty() || _uiState.value.phone.isEmpty() || _uiState.value.password.isEmpty() || _uiState.value.confirmPassword.isEmpty() || _uiState.value.firstName.isEmpty() || _uiState.value.lastNames.isEmpty() || _uiState.value.year.isEmpty() || _uiState.value.month.isEmpty() || _uiState.value.year.isEmpty()) {
            _uiState.update {
                it.copy(message = Constantes.camposVaciosMessage)
            }
        } else {
            val credentialRequest = CredentialRequest(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.confirmPassword
            )

            viewModelScope.launch {
                if (_uiState.value.selectedUserType == UserType.Cliente) {
                    credentialService.register(credentialRequest)
                } else {
                    credentialService.registerAccountant(credentialRequest)
                }.flatMapConcat { credentialResult ->
                    if (credentialResult is NetworkResultt.Success) {

                        chatService.createChatDocument(credentialRequest.email)

                        if (_uiState.value.selectedUserType == UserType.Cliente) {
                            clientService.addClient(
                                Client(
                                    email = uiState.value.email,
                                    phone = uiState.value.phone,
                                    firstName = uiState.value.firstName,
                                    lastName = uiState.value.lastNames,
                                    dateOfBirth = LocalDate.of(
                                        uiState.value.year.toInt(),
                                        uiState.value.month.toInt(),
                                        uiState.value.day.toInt()
                                    ),
                                    accountantEmail = null
                                )
                            )
                        } else {
                            accountantService.addAccountant(
                                Accountant(
                                    email = uiState.value.email,
                                    phone = uiState.value.phone,
                                    firstName = uiState.value.firstName,
                                    lastName = uiState.value.lastNames,
                                    dateOfBirth = LocalDate.of(
                                        uiState.value.year.toInt(),
                                        uiState.value.month.toInt(),
                                        uiState.value.day.toInt()
                                    )
                                )
                            )
                        }
                    } else {
                        // Si la primera llamada no fue exitosa, se propaga el resultado
                        flowOf(credentialResult)
                    }
                }.catch { cause ->
                    _uiState.update {
                        it.copy(
                            message = cause.message,
                            isLoading = false
                        )
                    }
                }.collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> {
                            _uiState.update {
                                it.copy(
                                    email = "",
                                    password = "",
                                    confirmPassword = "",
                                    firstName = "",
                                    lastNames = "",
                                    phone = "",
                                    year = "",
                                    month = "",
                                    day = "",
                                    message = Constantes.registrado,
                                    isLoading = false
                                )
                            }
                        }

                        is NetworkResultt.Error -> {
                            _uiState.update {
                                it.copy(
                                    message = result.message,
                                    isLoading = false
                                )
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
}
