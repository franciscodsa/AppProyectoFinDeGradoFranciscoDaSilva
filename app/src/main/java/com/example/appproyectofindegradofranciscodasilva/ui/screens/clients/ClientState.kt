package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos.FileFilter

data class ClientState(
    val clients: List<Client> = emptyList(),
    val expandedClientId: String? = null,
    val message: String? = null,
    val accountantEmails: List<String> = listOf("accountant1@example.com", "accountant2@example.com"),
    val selectedAccountantEmail: String = "",
    val isLoading: Boolean = false,
    val selectedFilter: ClientFilter = ClientFilter.Todos,
)

enum class ClientFilter {
    Todos, NoAsignados
}
