package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

import com.example.appproyectofindegradofranciscodasilva.data.model.Message

data class ChatState(
    val clientEmail: String = "",
    val message: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)