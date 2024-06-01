package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun registerUser(credentialRequest: CredentialRequest): Flow<NetworkResultt<Unit>>{
        return flow {
            try {
                emit(NetworkResultt.Loading())
                val result = auth.createUserWithEmailAndPassword(credentialRequest.email, credentialRequest.password).await()
                if (result.user != null) {
                    emit(NetworkResultt.Success(Unit))
                } else {
                    emit(NetworkResultt.Error("Registration failed"))
                }
            } catch (e: Exception) {
                emit(NetworkResultt.Error(e.message ?: "An error occurred"))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun loginUser(email: String, password: String): Flow<NetworkResultt<Unit>>{
        return flow {
            try {
                emit(NetworkResultt.Loading())
                val result = auth.signInWithEmailAndPassword(email, password).await()
                if (result.user != null) {
                    emit(NetworkResultt.Success(Unit))
                } else {
                    emit(NetworkResultt.Error("Login failed"))
                }
            } catch (e: Exception) {
                emit(NetworkResultt.Error(e.message ?: "An error occurred"))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun changePassword(email: String, newPassword: String): Flow<NetworkResultt<Unit>> {
        return flow {
            try {
                emit(NetworkResultt.Loading())
                val user = auth.currentUser
                user?.updatePassword(newPassword)?.await()
                emit(NetworkResultt.Success(Unit))
            } catch (e: Exception) {
                emit(NetworkResultt.Error(e.message ?: "An error occurred"))
            }
        }.flowOn(Dispatchers.IO)
    }
}
