package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.repositories.ClientRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ClientService @Inject constructor(
    private val clientRepository: ClientRepository,
    private val tokenManager: TokenManager
) {
    fun addClient(client: Client): Flow<NetworkResultt<ApiMessage>>{
        return clientRepository.addClient(client)
    }

    fun updateClient(client: Client): Flow<NetworkResultt<ApiMessage>>{
        return clientRepository.updateClient(client)
    }

    fun getClients(): Flow<NetworkResultt<List<Client>>> {
       return clientRepository.getClients()
    }

    fun getClientsWithNoAccountant(): Flow<NetworkResultt<List<Client>>> {
        return clientRepository.getClientsWithNoAccountant()
    }

    suspend fun getClientsByAccountant(): Flow<NetworkResultt<List<Client>>> {
        return clientRepository.getClientsByAccount(tokenManager.getCurrentUser().first()?: "")
    }
}