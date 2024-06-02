package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

sealed class ChatEvent {
    data class OnMessageTextChange(val message: String) : ChatEvent()
    data class OnClientEmailChange(val email: String) : ChatEvent()
    object SendMessage : ChatEvent()
    object LoadMessages : ChatEvent()
}