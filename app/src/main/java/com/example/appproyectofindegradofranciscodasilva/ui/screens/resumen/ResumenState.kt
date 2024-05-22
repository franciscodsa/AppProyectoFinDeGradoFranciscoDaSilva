package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import java.io.File

data class ResumenState(
    val selectedTrimester: String = "",
    val selectedYear: String = "",
    val trimestres: List<String> = listOf(
        "T1",
        "T2",
        "T3",
        "T4"
    ),
    val userRole: String= "",
    val years: List<String> = emptyList(),
    val expandedTrimestre: Boolean = false,
    val expandedYear: Boolean = false,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val iva: Double = 0.0,
    val irpf: Double = 0.0,

    val selectedFile: File? = null,
    val mimeType: String = "",

    val newInvoiceIva: String = "",
    val newInvoiceTotal: String = "",
    val newInvoiceDescription: String = "",
    val isExpense: Boolean = false,


    val isLoading: Boolean = false,
    val message: String? = null
)