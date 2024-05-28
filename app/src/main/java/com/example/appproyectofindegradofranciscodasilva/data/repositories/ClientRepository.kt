package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.source.ClientDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val clientDataSource: ClientDataSource
) {
    fun addClient(client: Client): Flow<NetworkResultt<ApiMessage>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.addClient(client)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun updateClient(client: Client): Flow<NetworkResultt<ApiMessage>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.updateClient(client)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    fun getClients(): Flow<NetworkResultt<List<Client>>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.getClients()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getClientsByAccount(email : String): Flow<NetworkResultt<List<Client>>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.getClientsByAccount(email)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    fun getClientsWithNoAccountant(): Flow<NetworkResultt<List<Client>>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.getClientsWithNoAccountant()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}