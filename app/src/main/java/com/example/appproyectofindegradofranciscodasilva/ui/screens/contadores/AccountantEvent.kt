package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

sealed class AccountantEvent {
    object SetUserRole : AccountantEvent()
    class OnAccountantExpandChanged(val email: String) : AccountantEvent()
    object LoadAccountants : AccountantEvent()
    object MessageSeen : AccountantEvent()
    class DeleteAccountant(val email: String) : AccountantEvent()
}
