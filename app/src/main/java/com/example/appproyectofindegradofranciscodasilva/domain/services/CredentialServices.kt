package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse
import com.example.appproyectofindegradofranciscodasilva.data.repositories.CredentialRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CredentialServices @Inject constructor(
    private val tokenManager: TokenManager,
    private val credentialRepository: CredentialRepository
) {

    fun register(credentials: CredentialRequest): Flow<NetworkResultt<Boolean>> {
        return credentialRepository.register(credentials)
    }

    suspend fun login(credentials: CredentialRequest): Flow<NetworkResultt<LoginInfoResponse>> {
        val loginResult = credentialRepository.login(credentials)

        loginResult.collect { result ->
            if (result is NetworkResultt.Success) {
                result.data?.accessToken?.let { tokenManager.saveAccessToken(it) }
                result.data?.refreshToken?.let { tokenManager.saveRefreshToken(it) }
            }
        }

        return loginResult
    }
}