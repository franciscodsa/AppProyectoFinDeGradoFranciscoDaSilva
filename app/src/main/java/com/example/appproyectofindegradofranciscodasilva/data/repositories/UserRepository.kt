package com.example.appproyectofindegradofranciscodasilva.data.repositories

import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.User
import com.example.appproyectofindegradofranciscodasilva.data.source.UserDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {


    fun updateUser(user: User): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = userDataSource.updateUser(user)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    fun getUserById(email: String): Flow<NetworkResultt<User>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = userDataSource.getUserById(email)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteUser(email: String): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            emit(NetworkResultt.Loading())
            val result = userDataSource.deleteUser(email)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}
