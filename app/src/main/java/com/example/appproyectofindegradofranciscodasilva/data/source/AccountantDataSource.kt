package com.example.appproyectofindegradofranciscodasilva.data.source

import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.AccountantApiService
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject

class AccountantDataSource @Inject constructor(
    private val accountantApiService: AccountantApiService,
    private val moshi: Moshi
) {
    suspend fun getAccountants(): NetworkResultt<List<Accountant>> {
        try {
            val response = accountantApiService.getAllAccountants()

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

    suspend fun getByClientEmail(clientEmail: String): NetworkResultt<Accountant> {
        try {
            val response = accountantApiService.getByClientEmail(clientEmail)

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


    suspend fun addAccountant(accountant: Accountant): NetworkResultt<ApiMessage> {
        try {
            val response = accountantApiService.addAccountant(accountant)

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