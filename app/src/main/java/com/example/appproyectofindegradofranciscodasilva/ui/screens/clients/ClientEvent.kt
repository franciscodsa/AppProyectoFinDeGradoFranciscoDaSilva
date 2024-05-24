package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import com.example.appproyectofindegradofranciscodasilva.data.model.Client

sealed class ClientEvent {
    class OnClientExpandChanged(val clientId: String) : ClientEvent()
    class OnAccountantEmailChanged(val email: String) : ClientEvent()
    class OnSaveAccountantEmail(val client: Client) : ClientEvent()
    object LoadClients: ClientEvent()
    object LoadClientsWithNoAccountant: ClientEvent()
    class OnAccountantEmailSelected(val email: String) : ClientEvent()
}

