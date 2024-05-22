package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import com.example.appproyectofindegradofranciscodasilva.data.model.Client

data class ClientState(
    val clients: List<Client> = emptyList(),
    val expandedClientId: String? = null,
    val message: String? = null
)
