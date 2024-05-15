package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApiServices {
    @POST("upload")
    @Multipart
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("clientEmail") clientEmail: RequestBody
    ): Response<String>
}