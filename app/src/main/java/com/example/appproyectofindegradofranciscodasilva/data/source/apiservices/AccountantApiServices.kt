package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import retrofit2.Response
import retrofit2.http.GET

interface AccountantApiServices {

    @GET("accountant")
    suspend fun getAllAccountants() : Response<List<Accountant>>
}