package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.User

import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @POST("users/update")
    suspend fun updateUser(@Body user: User): Response<ApiMessage>

    @GET("users/{email}")
    suspend fun getUserById(@Path("email") email: String): Response<User>

    @DELETE("users/{email}")
    suspend fun deleteUser(@Path("email") email: String): Response<ApiMessage>
}