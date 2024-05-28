package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query


interface ClientApiServices {
    @POST("clients")
    suspend fun addClient(@Body client: Client): Response<ApiMessage>

    @POST("clients/update")
    suspend fun updateClient(@Body client: Client): Response<ApiMessage>

    @GET("clients")
    suspend fun getAllClients(): Response<List<Client>>

    @GET("clients/noAccountant")
    suspend fun getClientsWithNoAccountant(): Response<List<Client>>

    @GET("clients/byAccountant")
    suspend fun getClientsByAccountant(@Query("accountantEmail") accountantEmail: String): Response<List<Client>>


    @GET("clients/{clientEmail}")
    fun getClientByEmail(@Path("clientEmail") clientEmail: String): Response<Client>
}