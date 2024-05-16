package com.example.appproyectofindegradofranciscodasilva.data.repositories

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.source.FileDataSource
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

class FileRepository @Inject constructor(
    private val fileDataSource: FileDataSource
) {
    fun upload(file: File, mimeType: String, description: String, clientEmail: String): Flow<NetworkResultt<ApiMessage>> {
        return flow {
            Log.i("repo", file.name)
            emit(NetworkResultt.Loading())
            val result = fileDataSource.upload(file, mimeType, description, clientEmail)
            Log.i("repodespdata", file.name)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}