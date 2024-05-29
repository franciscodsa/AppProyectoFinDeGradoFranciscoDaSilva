package com.example.appproyectofindegradofranciscodasilva.ui.screens.register

sealed class RegisterEvent {

    object Register : RegisterEvent()

    class OnFirstNameTextChange(val name: String) : RegisterEvent()

    class OnLastNameChange(val lastName: String) : RegisterEvent()

    class OnEmailTextChange(val email: String) : RegisterEvent()

    class OnPhoneTextChange(val phone: String) : RegisterEvent()

    class OnPasswordTextChange(val password: String) : RegisterEvent()

    class OnPasswordConfirmTextChange(val confirmPassword: String) : RegisterEvent()

    class OnYearFieldChange(val year: String) : RegisterEvent()

    class OnMonthFieldChange(val month: String) : RegisterEvent()

    class OnSelectedUserType(val selectedUserType: UserType) : RegisterEvent()

    class OnDayFieldChange(val day: String) : RegisterEvent()

    object MessageSeen : RegisterEvent()

    object SetUserRole : RegisterEvent()
}