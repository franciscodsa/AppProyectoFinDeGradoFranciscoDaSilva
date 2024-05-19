package com.example.appproyectofindegradofranciscodasilva.data.model

data class Balance(
    val income: Double,
    val expenses: Double,
    val iva: Double,
    var clientEmail: String?
)
