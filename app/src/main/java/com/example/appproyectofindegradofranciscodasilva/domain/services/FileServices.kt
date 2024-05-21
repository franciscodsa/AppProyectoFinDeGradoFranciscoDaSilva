package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.content.Context
import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.InvoiceType
import com.example.appproyectofindegradofranciscodasilva.data.repositories.FileRepository
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject

class FileServices @Inject constructor(
    private val fileRepository: FileRepository,
    private val tokenManager: TokenManager
) {



    suspend fun upload(file: File, mimeType: String, description: String, invoiceType: InvoiceType) : Flow<NetworkResultt<ApiMessage>>{
        val clientEmail = tokenManager.getCurrentUser().first() ?:""

        Log.i("serv", file.name)
        return fileRepository.upload(file, mimeType, description, clientEmail, invoiceType)
    }


    //todo: eliminar cuando cambies la pantalla de files a solo descargar, el resumen usa el de arriba
    fun upload(file: File, mimeType: String, description: String, clientEmail: String, invoiceType: InvoiceType) : Flow<NetworkResultt<ApiMessage>>{

        Log.i("serv", file.name)
        return fileRepository.upload(file, mimeType, description, clientEmail, invoiceType)
    }

    fun download(fileId : Long, context: Context):Flow<NetworkResultt<String>>{
        return fileRepository.download(fileId, context)
    }
}