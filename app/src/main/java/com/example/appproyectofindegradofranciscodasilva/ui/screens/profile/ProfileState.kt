package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

data class ProfileState(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val year: String = "",
    val month: String = "",
    val day: String = "",
    val message: String? = null,
    val isLoadin: Boolean = false
)
