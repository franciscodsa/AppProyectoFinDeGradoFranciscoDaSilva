package com.example.appproyectofindegradofranciscodasilva.ui.screens.contacts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.domain.services.AccountantServices
import com.example.appproyectofindegradofranciscodasilva.domain.services.ClientServices
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val accountantServices: AccountantServices,
    private val clientServices: ClientServices,
    private val credentialServices: CredentialServices
) : ViewModel() {


    private val _uiState = MutableStateFlow(ContactsState())
    val uiState: StateFlow<ContactsState> = _uiState.asStateFlow()


    fun handleEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.LoadContacts -> loadContacts()
            ContactsEvent.LoadCurrentUser -> loadCurrentUser()
            ContactsEvent.SetUserRole -> setRole()
            ContactsEvent.MessageSeen ->  _uiState.update {
                it.copy(
                    message = null
                )
            }
        }
    }

    private fun setRole() {
        viewModelScope.launch {
            val role = credentialServices.getRole()
            _uiState.update { it.copy(userRole = role) }
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val role = _uiState.value.userRole

            Log.i("ASDASDASDASDASDASDA", role)
            when (role) {
                "user" -> {
                    loadAccountant()
                }
                "accountant" -> {
                    loadClientsByAccountant()
                }
                else -> {
                    loadClients()
                }
            }
        }
    }

    private fun loadAccountant() {
        viewModelScope.launch {
            accountantServices.getByClientEmail()
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(message = result.message, isLoading = false)
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }

                        is NetworkResultt.Success -> _uiState.update {
                            it.copy(accountant = result.data?: Accountant(
                                "contador@contaeasy.com",
                                "",
                                "Contador",
                                "",
                                LocalDate.now()
                            ), isLoading = false)
                        }
                    }

                }
        }
    }

    private fun loadClientsByAccountant() {
        viewModelScope.launch {
            clientServices.getClientsByAccountant()
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update {
                            it.copy(clients = result.data ?: emptyList(), isLoading = false)
                        }

                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(message = result.message, isLoading = false)
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }

    private fun loadClients() {
        viewModelScope.launch {
            clientServices.getClients()
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update {
                            it.copy(clients = result.data ?: emptyList(), isLoading = false)
                        }

                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(message = result.message, isLoading = false)
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val currentUser = credentialServices.getCurrentUser()
            _uiState.update { it.copy(currentUser = currentUser) }
        }
    }
}


