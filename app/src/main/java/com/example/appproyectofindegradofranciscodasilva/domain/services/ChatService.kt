package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Message
import com.example.appproyectofindegradofranciscodasilva.data.repositories.ChatRepository
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class ChatService @Inject constructor(
    private val chatRepository: ChatRepository
) {

    /*  fun createChatDocument(clientEmail: String): String {
          val chatRef = firebaseFirestore.collection("chats").document(clientEmail)
          val chatData = hashMapOf(
              "clientEmail" to clientEmail
          )

          val result = chatRef.set(chatData)

          return if (result.isSuccessful) {
              "Creado"
          } else {
              "Error creando chat"
          }
      }*/

    fun createChatDocument(clientEmail: String) {
        chatRepository.createChatDocument(clientEmail)
    }

    fun sendMessage(
        clientEmail: String,
        senderEmail: String,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val message = hashMapOf(
            "senderEmail" to senderEmail,
            "content" to content,
            "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )
        chatRepository.addMessage(clientEmail, message, onSuccess, onFailure)
    }

    fun loadMessages(
        clientEmail: String,
        onMessagesLoaded: (List<Message>) -> Unit,
        onFailure: (Exception) -> Unit
    ): ListenerRegistration {
        return chatRepository.getMessages(clientEmail, onMessagesLoaded, onFailure)
    }

}