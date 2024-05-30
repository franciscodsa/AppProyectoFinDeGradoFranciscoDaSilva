package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

import com.example.appproyectofindegradofranciscodasilva.ui.screens.clients.ClientEvent

sealed class AccountantEvent {
    class OnAccountantExpandChanged(val email: String) : AccountantEvent()
    object LoadAccountants : AccountantEvent()
    object MessageSeen : AccountantEvent()
    class DeleteAccountant(val email: String) : AccountantEvent()
}
