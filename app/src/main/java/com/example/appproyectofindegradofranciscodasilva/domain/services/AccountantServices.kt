package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.repositories.AccountantRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountantServices @Inject constructor(
    private val accountantRepository: AccountantRepository
) {

    fun getAccountants(): Flow<NetworkResultt<List<Accountant>>> {
        return accountantRepository.getAccountants()
    }

    fun addAccountant(accountant: Accountant): Flow<NetworkResultt<ApiMessage>> {
        return accountantRepository.addAccountant(accountant)
    }
}