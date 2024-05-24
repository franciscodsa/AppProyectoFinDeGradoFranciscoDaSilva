package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.repositories.AccountantRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountantServices @Inject constructor(
    private val accountantRepository: AccountantRepository
) {

    fun getAccountants(): Flow<NetworkResultt<List<Accountant>>> {
        return accountantRepository.getAccountants()
    }
}