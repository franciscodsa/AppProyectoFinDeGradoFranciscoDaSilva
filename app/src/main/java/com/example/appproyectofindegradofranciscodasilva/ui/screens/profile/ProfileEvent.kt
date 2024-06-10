package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

sealed class ProfileEvent {
    data class OnFirstNameChanged(val firstName: String) : ProfileEvent()
    data class OnLastNameChanged(val lastName: String) : ProfileEvent()
    data class OnPhoneChanged(val phone: String) : ProfileEvent()
    data class OnYearFieldChange(val year: String) : ProfileEvent()
    data class OnMonthFieldChange(val month: String) : ProfileEvent()
    data class OnDayFieldChange(val day: String) : ProfileEvent()
    object SaveChanges : ProfileEvent()
    object Logout : ProfileEvent()
    object MessageSeen : ProfileEvent()
    object LoadUserData : ProfileEvent()
}
