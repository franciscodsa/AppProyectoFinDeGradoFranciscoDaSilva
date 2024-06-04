package com.example.appproyectofindegradofranciscodasilva.ui.screens.contacts

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.model.User
import java.time.LocalDate

data class ContactsState(
    val clients: List<Client> = emptyList(),
    val accountant: Accountant = Accountant(
        "contador@contaeasy",
        "",
        "Contador",
        "",
        LocalDate.now()
    ),
    val message: String? = null,
    val userRole: String = "",
    val currentUser: String = "",
    val isLoading: Boolean = false
)