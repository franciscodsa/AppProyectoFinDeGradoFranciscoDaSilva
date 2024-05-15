package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

sealed class LoginEvent {
    object Login : LoginEvent()

    class OnEmailTextChange(val email: String) : LoginEvent()

    class OnPasswordTextChange(val password: String) : LoginEvent()

    object MessageSeen : LoginEvent()
}
