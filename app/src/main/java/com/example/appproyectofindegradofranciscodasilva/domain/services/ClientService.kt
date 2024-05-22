package com.example.appproyectofindegradofranciscodasilva.domain.services

import com.example.appproyectofindegradofranciscodasilva.data.model.Client

import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class ClientService @Inject constructor() {

    fun getClients(): Flow<NetworkResultt<List<Client>>> = flow {
        emit(NetworkResultt.Loading())
        try {
            // Simula la obtención de clientes desde una API o base de datos
            val clients = listOf(
                Client("client1@example.com", "John", "Doe", "123456789", LocalDate.now(),  "accountant1@example.com"),
                Client("client2@example.com", "Jane", "Smith", "987654321", LocalDate.now(),  "accountant2@example.com")
            )
            emit(NetworkResultt.Success(clients))
        } catch (e: Exception) {
            emit(NetworkResultt.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
