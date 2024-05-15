package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Path


interface ClientApiServices {
    @POST("clients")
    suspend fun addClient(@Body client: Client): Response<Client>

    @GET("clients")
    suspend fun getAllClients(): Response<List<Client>>

    @GET("clientes/{clientEmail}")
    fun getClientByEmail(@Path("clientEmail") clientEmail: String): Response<Client>
}