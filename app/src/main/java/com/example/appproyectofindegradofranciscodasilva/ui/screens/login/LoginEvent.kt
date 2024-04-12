package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

sealed class LoginEvent {
    object Login : LoginEvent()

    object Register : LoginEvent()

    class onUsernameTextChange(val username: String) : LoginEvent()

    class onPasswordTextChange(val password: String) : LoginEvent()

    object MessageSeen : LoginEvent()
}
