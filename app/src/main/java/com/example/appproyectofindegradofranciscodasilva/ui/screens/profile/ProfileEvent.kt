package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

sealed class ProfileEvent {
    data class OnFirstNameChanged(val firstName: String) : ProfileEvent()
    data class OnLastNameChanged(val lastName: String) : ProfileEvent()
    data class OnPhoneChanged(val phone: String) : ProfileEvent()
    data class OnDateOfBirthChanged(val dateOfBirth: String) : ProfileEvent()
    object SaveChanges : ProfileEvent()
    object Logout : ProfileEvent()
}
