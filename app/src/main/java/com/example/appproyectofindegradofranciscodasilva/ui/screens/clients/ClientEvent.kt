package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

sealed class ClientEvent {
    data class OnClientExpandChanged(val clientId: String) : ClientEvent()
    data class OnAccountantEmailChanged(val clientId: String, val email: String) : ClientEvent()
    data class OnSaveAccountantEmail(val clientId: String) : ClientEvent()

}

