package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

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
        val senderEmail = getCurrentUserEmail()
        val clientEmail = _uiState.value.clientEmail

        if (senderEmail.isNullOrEmpty() || clientEmail.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "User not logged in or client email not set", isLoading = false) }
            return
        }

        if (_uiState.value.message.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Message cannot be empty", isLoading = false) }
            return
        }

        viewModelScope.launch {
            val message = hashMapOf(
                "senderEmail" to senderEmail,
                "content" to _uiState.value.message,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("chats").document(clientEmail)
                .collection("messages").add(message)
                .addOnSuccessListener {
                    _uiState.update { it.copy(message = "", isLoading = false) }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(errorMessage = "Error sending message: ${e.message}", isLoading = false) }
                }
        }
    }

    private fun loadMessages() {
        val clientEmail = _uiState.value.clientEmail

        if (clientEmail.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Client email not set", isLoading = false) }
            return
        }

        db.collection("chats").document(clientEmail).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    _uiState.update { it.copy(errorMessage = "Listen failed: ${e.message}", isLoading = false) }
                    return@addSnapshotListener
                }

                val messages = snapshots?.documents?.map { document ->
                    Message(
                        senderEmail = document.getString("senderEmail") ?: "",
                        content = document.getString("content") ?: "",
                        timestamp = document.getTimestamp("timestamp")?.toDate() ?: Date()
                    )
                } ?: emptyList()

                _uiState.update { it.copy(messages = messages, isLoading = false) }
            }
    }

    private fun getCurrentUserEmail(): String? {
        val user = auth.currentUser
        return user?.email
    }
}
