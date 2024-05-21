package com.example.appproyectofindegradofranciscodasilva.data.source

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.QuarterBalance
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.BalanceApiServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject

class BalanceDataSource @Inject constructor(
    private val balanceApiServices: BalanceApiServices,
    private val moshi: Moshi
) {
    suspend fun addBalance(balance: Balance): NetworkResultt<ApiMessage> {
        try {
            val response = balanceApiServices.addBalance(balance)

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
            Log.e("error", e.message.toString())
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun getBalancesByClientIdAndYearAndQuarter(
        clientEmail: String,
        year: Int,
        quarter: String
    ): NetworkResultt<QuarterBalance> {
        try {
            val response = balanceApiServices.getBalancesByClientIdAndYearAndQuarter(clientEmail, year, quarter)

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
            Log.e("error", e.message.toString())
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }


}