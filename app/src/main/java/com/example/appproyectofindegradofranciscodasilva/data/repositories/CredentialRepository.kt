package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse
import com.example.appproyectofindegradofranciscodasilva.data.source.CredentialsDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CredentialRepository @Inject constructor(
    private val credentialsDataSource: CredentialsDataSource
) {
    fun login(credentials: CredentialRequest): Flow<NetworkResultt<LoginInfoResponse>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = credentialsDataSource.login(credentials)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun register(credentials: CredentialRequest): Flow<NetworkResultt<Boolean>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = credentialsDataSource.register(credentials)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun registerAccountant(credentials: CredentialRequest): Flow<NetworkResultt<Boolean>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = credentialsDataSource.registerAccountant(credentials)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getAuthCode(email: String): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = credentialsDataSource.getAuthCode(email)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun changePassword(
        password: String,
        passwordConfirmation: String,
        authCode: String
    ): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result =
                credentialsDataSource.changePassword(password, passwordConfirmation, authCode)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteCredentials(clientEmail: String): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = credentialsDataSource.deleteCredentials(clientEmail)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}
