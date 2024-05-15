package com.example.appproyectofindegradofranciscodasilva.data.repositories

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
    fun addClient(client: Client): Flow<NetworkResultt<Client>>{
        return flow {
            emit(NetworkResultt.Loading())
            val result= clientDataSource.addClient(client)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}