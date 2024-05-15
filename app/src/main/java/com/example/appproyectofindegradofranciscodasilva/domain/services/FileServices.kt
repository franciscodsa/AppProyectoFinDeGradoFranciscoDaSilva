package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.repositories.FileRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class FileServices @Inject constructor(
    private val fileRepository: FileRepository
) {

    fun upload(file: File, description: String, clientEmail: String) : Flow<NetworkResultt<String>>{
        Log.i("serv", file.name)
        return fileRepository.upload(file, description, clientEmail)
    }
}