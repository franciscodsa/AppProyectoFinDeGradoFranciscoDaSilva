package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.User
import com.example.appproyectofindegradofranciscodasilva.data.repositories.UserRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserServices @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) {


    fun updateUser(user: User): Flow<NetworkResultt<ApiMessage>> {
        return userRepository.updateUser(user)
    }

    suspend fun getUserByEmail(): Flow<NetworkResultt<User>> {
        return userRepository.getUserById(tokenManager.getCurrentUser().first()?:"")
    }

    fun deleteUser(email: String): Flow<NetworkResultt<ApiMessage>> {
        return userRepository.deleteUser(email)
    }
}
