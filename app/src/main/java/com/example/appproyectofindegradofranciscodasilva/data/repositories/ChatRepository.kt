package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.Date
import javax.inject.Inject


class ChatRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun addMessage(clientEmail: String, message: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("chats").document(clientEmail)
            .collection("messages").add(message)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getMessages(clientEmail: String, onMessagesLoaded: (List<Message>) -> Unit, onFailure: (Exception) -> Unit): ListenerRegistration {
        return db.collection("chats").document(clientEmail).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    onFailure(e)
                    return@addSnapshotListener
                }

                val messages = snapshots?.documents?.map { document ->
                    Message(
                        senderEmail = document.getString("senderEmail") ?: "",
                        content = document.getString("content") ?: "",
                        timestamp = document.getTimestamp("timestamp")?.toDate() ?: Date()
                    )
                } ?: emptyList()

                onMessagesLoaded(messages)
            }
    }

    fun createChatDocument(clientEmail: String) {
        val chatRef = db.collection("chats").document(clientEmail)
        val chatData = hashMapOf(
            "clientEmail" to clientEmail
        )

        chatRef.set(chatData)
    }
}