package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.repositories.ClientRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientServices @Inject constructor(
    private val clientRepository: ClientRepository
) {
    fun addClient(client: Client): Flow<NetworkResultt<Client>>{
        return clientRepository.addClient(client)
    }
}