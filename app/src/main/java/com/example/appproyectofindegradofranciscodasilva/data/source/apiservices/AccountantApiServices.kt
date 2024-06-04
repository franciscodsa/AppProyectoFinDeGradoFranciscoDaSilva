package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountantApiServices {

    @GET("accountant")
    suspend fun getAllAccountants() : Response<List<Accountant>>

    @POST("accountant")
    suspend fun addAccountant(@Body accountant: Accountant) : Response<ApiMessage>

    @GET("accountant/byClientEmail")
    suspend fun getByClientEmail(@Query("clientEmail") clientEmail: String): Response<Accountant>
}