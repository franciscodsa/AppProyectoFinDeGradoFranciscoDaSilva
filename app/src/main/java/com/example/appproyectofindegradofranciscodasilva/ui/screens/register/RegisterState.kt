package com.example.appproyectofindegradofranciscodasilva.ui.screens.register


data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastNames: String = "",
    val phone: String = "",
    val year: String= "",
    val month: String= "",
    val day: String= "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val userRole: String= "",
    val selectedUserType: UserType = UserType.Cliente
)

enum class UserType{
    Cliente, Contador
}
