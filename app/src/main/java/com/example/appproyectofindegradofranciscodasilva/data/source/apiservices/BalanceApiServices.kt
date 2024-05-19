package com.example.appproyectofindegradofranciscodasilva.data.source.apiservices

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.QuarterBalance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BalanceApiServices {

    @GET("balances/quarter")
    suspend fun getBalancesByClientIdAndYearAndQuarter(
        @Query("clientEmail") clientEmail: String,
        @Query("year") year: Int,
        @Query("quarter") quarter: String
    ): Response<QuarterBalance>

    @DELETE("balances/delete-quarter")
    fun deleteBalancesByClientIdAndYearAndQuarter(
        @Query("clientEmail") clientEmail: String,
        @Query("year") year: Int,
        @Query("quarter") quarter: String
    ): Response<Void>

    @POST("balances")
    fun addBalance(@Body balance: Balance): Response<ApiMessage>
}