package com.example.appproyectofindegradofranciscodasilva.data.repositories

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

}
