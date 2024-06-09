package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse
import com.example.appproyectofindegradofranciscodasilva.data.repositories.CredentialRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CredentialService @Inject constructor(
    private val tokenManager: TokenManager,
    private val credentialRepository: CredentialRepository
) {

    fun register(credentials: CredentialRequest): Flow<NetworkResultt<Boolean>> {
        return credentialRepository.register(credentials)
    }

    fun registerAccountant(credentials: CredentialRequest): Flow<NetworkResultt<Boolean>> {
        return credentialRepository.registerAccountant(credentials)
    }

    suspend fun login(credentials: CredentialRequest): Flow<NetworkResultt<LoginInfoResponse>> {
        val loginResult = credentialRepository.login(credentials)

        loginResult.collect { result ->
            if (result is NetworkResultt.Success) {
                result.data?.accessToken?.let { tokenManager.saveAccessToken(it) }
                result.data?.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                result.data?.role?.let { tokenManager.saveRole(it) }
                tokenManager.saveCurrentUser(credentials.email)

                Log.i("AAAAAAAAAaaa",tokenManager.getCurrentUser().first().toString() + "Login")
            }
        }

        return loginResult
    }

    suspend fun getRole(): String{
        return tokenManager.getRole().first()?:""
    }

    suspend fun getCurrentUser(): String{
        return tokenManager.getCurrentUser().first()?:""
    }

    suspend fun logout() {
        tokenManager.clearStoredData()
    }

    fun getAuthCode(email: String): Flow<NetworkResultt<ApiMessage>> {
        return credentialRepository.getAuthCode(email)
    }

    fun changePassword(password: String, passwordConfirmation: String, authCode: String): Flow<NetworkResultt<ApiMessage>> {
        return credentialRepository.changePassword(password, passwordConfirmation, authCode)
    }

    fun deleteCredentials(clientEmail: String): Flow<NetworkResultt<ApiMessage>>{
        return credentialRepository.deleteCredentials(clientEmail)
    }
}