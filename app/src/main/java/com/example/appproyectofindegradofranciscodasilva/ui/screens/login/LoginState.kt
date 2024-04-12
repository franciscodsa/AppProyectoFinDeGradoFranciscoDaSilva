package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val logged: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)
