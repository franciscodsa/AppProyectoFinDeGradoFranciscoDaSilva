package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.domain.services.ClientService
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
class ClientViewModel @Inject constructor(
    private val clientService: ClientService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientState())
    val uiState: StateFlow<ClientState> = _uiState.asStateFlow()

    init {
        loadClients()
    }

    fun handleEvent(event: ClientEvent) {
        when (event) {
            is ClientEvent.OnClientExpandChanged -> _uiState.update {
                it.copy(expandedClientId = if (it.expandedClientId == event.clientId) null else event.clientId)
            }
            is ClientEvent.OnAccountantEmailChanged -> _uiState.update { state ->
                state.copy(
                    clients = state.clients.map {
                        if (it.email == event.clientId) it.copy(accountantEmail = event.email) else it
                    }
                )
            }
            is ClientEvent.OnSaveAccountantEmail -> saveAccountantEmail(event.clientId)
        }
    }

    private fun loadClients() {
        viewModelScope.launch {
            clientService.getClients()
                .catch { cause -> _uiState.update { it.copy(message = cause.message) } }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update { it.copy(clients = result.data?: emptyList()) }
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message) }
                        is NetworkResultt.Loading -> {} // Handle loading state if needed
                    }
                }
        }
    }

    private fun saveAccountantEmail(clientId: String) {
        // Implement logic to save accountant email
    }
}
