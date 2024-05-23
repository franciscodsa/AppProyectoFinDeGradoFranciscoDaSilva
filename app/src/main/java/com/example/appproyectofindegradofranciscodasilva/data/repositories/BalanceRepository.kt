package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.QuarterBalance
import com.example.appproyectofindegradofranciscodasilva.data.source.BalanceDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val balanceDataSource: BalanceDataSource,
){
    fun updateBalance(balance: Balance): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = balanceDataSource.updateBalance(balance)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getBalancesByClientIdAndYearAndQuarter(clientEmail: String, year: Int, quarter: String): Flow<NetworkResultt<QuarterBalance>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result = balanceDataSource.getBalancesByClientIdAndYearAndQuarter(clientEmail, year, quarter)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}