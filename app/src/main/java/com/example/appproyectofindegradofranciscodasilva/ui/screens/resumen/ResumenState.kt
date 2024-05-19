package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

data class ResumenState(
    val selectedTrimester: String = "",
    val selectedYear: String = "",
    val trimestres: List<String> = listOf(
        "T1",
        "T2",
        "T3",
        "T4"
    ),
    val years: List<String> = emptyList(),
    val expandedTrimestre: Boolean = false,
    val expandedYear: Boolean = false,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val iva: Double = 0.0,
    val irpf: Double = 0.0,
    val isLoading: Boolean = false,
    val message: String? = null
)