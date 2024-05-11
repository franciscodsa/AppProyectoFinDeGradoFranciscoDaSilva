package com.example.appproyectofindegradofranciscodasilva.data.model

data class CredentialRequest(
    val email: String,
    val password: String,
    val passwordConfirmation: String = ""
)