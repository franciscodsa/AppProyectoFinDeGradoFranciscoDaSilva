package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.content.Context
import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.repositories.FileRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class FileServices @Inject constructor(
    private val fileRepository: FileRepository
) {

    fun upload(file: File, mimeType: String, description: String, clientEmail: String) : Flow<NetworkResultt<ApiMessage>>{
        Log.i("serv", file.name)
        return fileRepository.upload(file, mimeType, description, clientEmail)
    }

    fun download(fileId : Long, context: Context):Flow<NetworkResultt<String>>{
        return fileRepository.download(fileId, context)
    }
}