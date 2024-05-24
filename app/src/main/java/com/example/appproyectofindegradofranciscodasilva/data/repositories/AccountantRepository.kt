package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.source.AccountantDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountantRepository @Inject constructor(
    private val accountantDataSource: AccountantDataSource
) {

    fun getAccountants(): Flow<NetworkResultt<List<Accountant>>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= accountantDataSource.getAccountants()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}