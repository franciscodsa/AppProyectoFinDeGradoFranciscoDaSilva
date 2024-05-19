package com.example.appproyectofindegradofranciscodasilva.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class QuarterBalance(
    val income: Double,

    val expenses: Double,

    val irpf: Double,

    val iva: Double,

    val date: LocalDateTime,

    val clientEmail: String
)
