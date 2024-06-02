package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.domain.services.ChatService
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialServices
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val credentialServices: CredentialServices
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        // Puedes cargar los mensajes iniciales si es necesario
        // loadMessages()
    }

    fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageTextChange -> _uiState.update { it.copy(message = event.message) }
            is ChatEvent.OnClientEmailChange -> _uiState.update { it.copy(clientEmail = event.email) }
            ChatEvent.SendMessage -> sendMessage()
            ChatEvent.LoadMessages -> loadMessages()
        }
    }

    private fun sendMessage() {
        val clientEmail = _uiState.value.clientEmail
        viewModelScope.launch {
            val senderEmail = credentialServices.getCurrentUser()
            if (senderEmail.isEmpty() || clientEmail.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "User not logged in or client email not set",
                        isLoading = false
                    )
                }
                return@launch
            }

            if (_uiState.value.message.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Message cannot be empty",
                        isLoading = false
                    )
                }
                return@launch
            }

            chatService.sendMessage(clientEmail, senderEmail, _uiState.value.message, {
                _uiState.update { it.copy(message = "", isLoading = false) }
            }, { e ->
                _uiState.update {
                    it.copy(
                        errorMessage = "Error sending message: ${e.message}",
                        isLoading = false
                    )
                }
            })
        }
    }

    private fun loadMessages() {
        val clientEmail = _uiState.value.clientEmail

        if (clientEmail.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Client email not set", isLoading = false) }
            return
        }

        // Elimina el listener anterior si existe
        listenerRegistration?.remove()

        listenerRegistration = chatService.loadMessages(clientEmail, { messages ->
            _uiState.update { it.copy(messages = messages, isLoading = false) }
        }, { e ->
            _uiState.update {
                it.copy(
                    errorMessage = "Listen failed: ${e.message}",
                    isLoading = false
                )
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        // Elimina el listener cuando el ViewModel se destruye
        listenerRegistration?.remove()
    }
}
