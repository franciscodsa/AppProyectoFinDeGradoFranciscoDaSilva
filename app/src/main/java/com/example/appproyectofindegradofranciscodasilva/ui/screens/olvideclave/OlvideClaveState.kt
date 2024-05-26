package com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave

data class OlvideClaveState(
    val email: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val authCode: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)
