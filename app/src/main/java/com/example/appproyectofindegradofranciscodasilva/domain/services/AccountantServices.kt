package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.repositories.AccountantRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AccountantServices @Inject constructor(
    private val accountantRepository: AccountantRepository,
    private val tokenManager: TokenManager
) {

    fun getAccountants(): Flow<NetworkResultt<List<Accountant>>> {
        return accountantRepository.getAccountants()
    }

    suspend fun getByClientEmail(): Flow<NetworkResultt<Accountant>> {
        return accountantRepository.getByClientEmail(tokenManager.getCurrentUser().first() ?: "")
    }

    fun addAccountant(accountant: Accountant): Flow<NetworkResultt<ApiMessage>> {
        return accountantRepository.addAccountant(accountant)
    }
}