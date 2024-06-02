package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.repositories.FirebaseRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//todo borrar si al final no resulta necesario
class FirebaseService @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

    fun registerUser(credentialRequest: CredentialRequest): Flow<NetworkResultt<Unit>> {
        return firebaseRepository.registerUser(credentialRequest)
    }

    fun loginUser(email: String, password: String): Flow<NetworkResultt<Unit>> {
        return firebaseRepository.loginUser(email, password)
    }

    fun changePassword(email: String, newPassword: String): Flow<NetworkResultt<Unit>> {
        return firebaseRepository.changePassword(email, newPassword)
    }
}
