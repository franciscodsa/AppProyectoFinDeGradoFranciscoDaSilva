package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatService @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    fun createChatDocument(clientEmail: String): String {
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
    }
}