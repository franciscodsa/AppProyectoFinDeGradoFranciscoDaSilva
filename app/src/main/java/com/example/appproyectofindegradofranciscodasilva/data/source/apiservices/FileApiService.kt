package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.FileInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FileApiService {
    @POST("files/upload")
    @Multipart
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("clientEmail") clientEmail: RequestBody,
        @Part("invoiceType") invoiceType: RequestBody,
        @Part("income") income: RequestBody,
        @Part("expenses") expenses: RequestBody,
        @Part("iva") iva: RequestBody
    ): Response<ApiMessage>


    @GET("files/download/{fileId}")
    suspend fun downloadFile(
        @Path("fileId") fileId: Long
    ): Response<ResponseBody>

    @GET("files/info")
    suspend fun getFilesByClient(@Query("clientEmail") clientEmail: String): Response<List<FileInfo>>

    @GET("files/expensesInfo")
    suspend fun getExpensesFilesByClient(@Query("clientEmail") clientEmail: String): Response<List<FileInfo>>

    @GET("files/incomeInfo")
    suspend fun getIncomeFilesByClient(@Query("clientEmail") clientEmail: String): Response<List<FileInfo>>

    @DELETE("files/delete/{fileId}")
    suspend fun deleteFile(@Path("fileId") fileId: Long): Response<ApiMessage>

}