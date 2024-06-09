package com.example.appproyectofindegradofranciscodasilva.data.source

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.ClientApiService
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ClientDataSource @Inject constructor(
    private val clientApiService: ClientApiService,
    private val moshi: Moshi
) {

    suspend fun addClient(client: Client): NetworkResultt<ApiMessage> {
        try {
            Log.i("asdasd", "AAAAAAAAAAAAAAAAAAAAAAA")

            val response = clientApiService.addClient(client)

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

    suspend fun updateClient(client: Client): NetworkResultt<ApiMessage> {
        try {
            Log.i("asdasd", "AAAAAAAAAAAAAAAAAAAAAAA")

            val response = clientApiService.updateClient(client)

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

    suspend fun getClients(): NetworkResultt<List<Client>> {
        try {
            val response = clientApiService.getAllClients()

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

    suspend fun getClientsWithNoAccountant(): NetworkResultt<List<Client>> {
        try {
            val response = clientApiService.getClientsWithNoAccountant()

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

    suspend fun getClientsByAccount(email: String): NetworkResultt<List<Client>> {
        try {
            val response = clientApiService.getClientsByAccountant(email)

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

