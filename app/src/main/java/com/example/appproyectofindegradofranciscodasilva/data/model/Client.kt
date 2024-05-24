package com.example.appproyectofindegradofranciscodasilva.data.model

import java.time.LocalDate


data class Client(
    val email: String,
    val phone: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val accountantEmail: String?
)