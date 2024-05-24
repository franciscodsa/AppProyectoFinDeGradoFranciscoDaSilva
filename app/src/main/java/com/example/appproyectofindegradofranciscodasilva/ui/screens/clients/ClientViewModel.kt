package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.domain.services.ClientServices
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
    private val clientService: ClientServices
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

            is ClientEvent.OnAccountantEmailChanged -> _uiState.update {
                it.copy(
                    selectedAccountantEmail = event.email
                )
            }

            is ClientEvent.OnSaveNewClientsAccountant -> updateClientAccountant(event.client)

            ClientEvent.LoadClients -> loadClients()
            ClientEvent.LoadClientsWithNoAccountant -> loadClientsWithNoAccountant()
            is ClientEvent.OnAccountantEmailSelected -> {
                _uiState.update { it.copy(selectedAccountantEmail = event.email) }
            }

            is ClientEvent.OnFilterChanged -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }

                when(event.filter){
                    ClientFilter.Todos -> loadClients()
                    ClientFilter.NoAsignados -> loadClientsWithNoAccountant()
                }
            }
        }
    }

    private fun updateClientAccountant(client: Client) {
        val updatedClient = client.copy(accountantEmail = _uiState.value.selectedAccountantEmail.ifEmpty { null })

        viewModelScope.launch {
            clientService.updateClient(updatedClient)
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
                                    message = "Actualizado",
                                    isLoading = false
                                )
                            }

                            when(_uiState.value.selectedFilter) {
                                ClientFilter.Todos -> loadClients()
                                ClientFilter.NoAsignados -> loadClientsWithNoAccountant()
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

    private fun loadClients() {
        viewModelScope.launch {
            clientService.getClients()
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
                                    clients = result.data ?: emptyList(),
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


    private fun loadClientsWithNoAccountant() {
        viewModelScope.launch {
            clientService.getClientsWithNoAccountant()
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
                                    clients = result.data ?: emptyList(),
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
