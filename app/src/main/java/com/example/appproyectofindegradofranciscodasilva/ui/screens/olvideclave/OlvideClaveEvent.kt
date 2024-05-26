package com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave

sealed class OlvideClaveEvent {
    data class OnEmailChange(val email: String) : OlvideClaveEvent()
    data class OnNewPasswordChange(val newPassword: String) : OlvideClaveEvent()
    data class OnConfirmPasswordChange(val confirmPassword: String) : OlvideClaveEvent()
    data class OnAuthCodeChange(val authCode: String) : OlvideClaveEvent()
    object OnSendEmail : OlvideClaveEvent()
    object OnChangePassword : OlvideClaveEvent()
    object MessageSeen : OlvideClaveEvent()
}
