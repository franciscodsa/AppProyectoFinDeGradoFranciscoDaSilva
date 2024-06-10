package com.example.appproyectofindegradofranciscodasilva.data.source

import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.QuarterBalance
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.BalanceApiService
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import javax.inject.Inject

class BalanceDataSource @Inject constructor(
    private val balanceApiService: BalanceApiService,
    private val moshi: Moshi
) {
    suspend fun updateBalance(balance: Balance): NetworkResultt<ApiMessage> {
        try {
            val response = balanceApiService.updateBalance(balance)

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

            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun getBalancesByClientIdAndYearAndQuarter(
        clientEmail: String,
        year: Int,
        quarter: String
    ): NetworkResultt<QuarterBalance> {
        try {
            val response =
                balanceApiService.getBalancesByClientIdAndYearAndQuarter(clientEmail, year, quarter)

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

            return NetworkResultt.Error(Constantes.unknownError)
        }
    }


}