package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant

data class AccountantState(
    val accountants: List<Accountant> = emptyList(),
    val expandedAccountantId: String? = null,
    val message: String? = null,
    val userRole: String = "",
    val isLoading: Boolean = false
)
