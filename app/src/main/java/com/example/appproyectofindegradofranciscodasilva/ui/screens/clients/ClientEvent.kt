package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos.FileEvent
import com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos.FileFilter

sealed class ClientEvent {
    class OnClientExpandChanged(val clientId: String) : ClientEvent()
    class OnAccountantEmailChanged(val email: String) : ClientEvent()
    class OnSaveNewClientsAccountant(val client: Client) : ClientEvent()
    object LoadClients: ClientEvent()
    object LoadClientsWithNoAccountant: ClientEvent()
    class OnAccountantEmailSelected(val email: String) : ClientEvent()
    class OnFilterChanged(val filter: ClientFilter) : ClientEvent()
}

