package com.example.appproyectofindegradofranciscodasilva.data.model

data class Balance(
    val id: Long,
    val income: Double,
    val expenses: Double,
    val iva: Double,
    var clientEmail: String?
)
