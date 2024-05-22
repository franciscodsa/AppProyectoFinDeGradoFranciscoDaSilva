package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.repositories.ClientRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class ClientServices @Inject constructor(
    private val clientRepository: ClientRepository
) {
    fun addClient(client: Client): Flow<NetworkResultt<Client>>{
        return clientRepository.addClient(client)
    }

    fun getClients(): Flow<NetworkResultt<List<Client>>> = flow {
        emit(NetworkResultt.Loading())
        try {
            // Simula la obtención de clientes desde una API o base de datos
            val clients = listOf(
                Client("client1@example.com", "123456789","John", "Doe",  LocalDate.now(),  "accountant1@example.com"),
                Client("client2@example.com", "987654321","Jane", "Smith",  LocalDate.now(),  "accountant2@example.com")
            )
            emit(NetworkResultt.Success(clients))
        } catch (e: Exception) {
            emit(NetworkResultt.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}