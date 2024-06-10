package com.example.appproyectofindegradofranciscodasilva.data.source

import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.User
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.UserApiService
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userApiService: UserApiService,
    private val moshi: Moshi
) {


    suspend fun updateUser(user: User): NetworkResultt<ApiMessage> {
        try {
            val response = userApiService.updateUser(user)

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

    suspend fun getUserById(email: String): NetworkResultt<User> {
        try {
            val response = userApiService.getUserById(email)

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

    suspend fun deleteUser(email: String): NetworkResultt<ApiMessage> {
        try {
            val response = userApiService.deleteUser(email)

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
