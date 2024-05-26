package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices


import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.CredentialRequest
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CredentialApiServices {
    @POST(Constantes.login)
    suspend fun login(@Body request: CredentialRequest): Response<LoginInfoResponse>

    @POST(Constantes.register)
    suspend fun register(@Body credentials: CredentialRequest): Response<Boolean>

    @POST(Constantes.registerAccountant)
    suspend fun registerAccountant(@Body credentials: CredentialRequest): Response<Boolean>


    @GET(Constantes.refreshTokenPath)
    suspend fun refreshAccessToken(@Query(Constantes.refreshToken) refreshToken: String): Response<LoginInfoResponse>

    @GET("authCode")
    suspend fun getAuthCode(@Query("email") email: String): Response<ApiMessage>

    @POST("updatePassword")
    suspend fun changePassword(
        @Query("password") password: String,
        @Query("passwordConfirmation") passwordConfirmation: String,
        @Query("authCode") authCode: String
    ): Response<ApiMessage>
}