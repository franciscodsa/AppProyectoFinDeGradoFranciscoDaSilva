package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

sealed class AccountantEvent {
    class OnAccountantExpandChanged(val email: String) : AccountantEvent()
    object LoadAccountants : AccountantEvent()
}
