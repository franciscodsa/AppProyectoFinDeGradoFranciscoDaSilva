package com.example.appproyectofindegradofranciscodasilva.data.source

import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.CredentialApiServices
import com.example.appproyectofindegradofranciscodasilva.data.source.di.CredentialServer


import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject


class CredentialsDataSource @Inject constructor(
    private val credentialApiService: CredentialApiServices,
    private val moshi: Moshi
) {

    suspend fun login(loginRequest: CredentialRequest): NetworkResultt<LoginInfoResponse> {
        try {
            val response = credentialApiService.login(loginRequest)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun register(loginRequest: CredentialRequest): NetworkResultt<Boolean> {
        try {
            val response = credentialApiService.register(loginRequest)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun registerAccountant(registerRequest: CredentialRequest): NetworkResultt<Boolean> {
        try {
            val response = credentialApiService.registerAccountant(registerRequest)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun getAuthCode(email: String): NetworkResultt<ApiMessage> {
        try {
            val response = credentialApiService.getAuthCode(email)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun changePassword(password: String, passwordConfirmation: String, authCode: String): NetworkResultt<ApiMessage> {
        try {
            val response = credentialApiService.changePassword(password, passwordConfirmation, authCode)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }
}