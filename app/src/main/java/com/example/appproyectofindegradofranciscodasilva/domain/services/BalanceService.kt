package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.QuarterBalance
import com.example.appproyectofindegradofranciscodasilva.data.repositories.BalanceRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

import javax.inject.Inject

class BalanceService @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val tokenManager: TokenManager
) {
    suspend fun updateBalance(balance: Balance): Flow<NetworkResultt<ApiMessage>> {
        if (balance.clientEmail.isNullOrEmpty()) {
            balance.clientEmail = tokenManager.getCurrentUser().first()
        }
        return balanceRepository.updateBalance(balance)
    }

    suspend fun getQuarterBalance(
        year: Int,
        quarter: String
    ): Flow<NetworkResultt<QuarterBalance>> {
        val clientEmail = tokenManager.getCurrentUser().first() ?: ""
        return balanceRepository.getBalancesByClientIdAndYearAndQuarter(clientEmail, year, quarter)
    }
}