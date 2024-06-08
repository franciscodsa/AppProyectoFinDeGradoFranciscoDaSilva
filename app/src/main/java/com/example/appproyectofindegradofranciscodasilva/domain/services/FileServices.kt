package com.example.appproyectofindegradofranciscodasilva.domain.services

import android.content.Context
import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.FileInfo
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


    suspend fun upload(
        file: File,
        mimeType: String,
        description: String,
        invoiceType: InvoiceType,
        balance: Balance
    ): Flow<NetworkResultt<ApiMessage>> {
        val clientEmail = tokenManager.getCurrentUser().first() ?: ""

        //todo quitar todos los log
        Log.i("AAAAAAAAAaaa",tokenManager.getCurrentUser().first().toString()+ "upload")
        return fileRepository.upload(file, mimeType, description, clientEmail, invoiceType, balance)
    }


    fun download(fileId: Long, context: Context): Flow<NetworkResultt<String>> {
        return fileRepository.download(fileId, context)
    }


    suspend fun getFilesByClient(): Flow<NetworkResultt<List<FileInfo>>> {
        val clientEmail = tokenManager.getCurrentUser().first() ?: ""
        Log.i("AAAAAAAAAaaa",tokenManager.getCurrentUser().first().toString()+ "clients")
        return fileRepository.getFilesByClient(clientEmail)
    }

    suspend fun getExpensesFilesByClient(): Flow<NetworkResultt<List<FileInfo>>> {
        val clientEmail = tokenManager.getCurrentUser().first() ?: ""
        Log.i("AAAAAAAAAaaa",tokenManager.getCurrentUser().first().toString()+ "expenses")

        return fileRepository.getExpensesFilesByClient(clientEmail)
    }

    suspend fun getIncomeFilesByClient(): Flow<NetworkResultt<List<FileInfo>>> {
        val clientEmail = tokenManager.getCurrentUser().first() ?: ""
        return fileRepository.getIncomeFilesByClient(clientEmail)
    }

    fun getFilesByClient(clientEmail: String): Flow<NetworkResultt<List<FileInfo>>> {
        return fileRepository.getFilesByClient(clientEmail)
    }

    fun getExpensesFilesByClient(clientEmail: String): Flow<NetworkResultt<List<FileInfo>>> {
        return fileRepository.getExpensesFilesByClient(clientEmail)
    }

    fun getIncomeFilesByClient(clientEmail: String): Flow<NetworkResultt<List<FileInfo>>> {

        return fileRepository.getIncomeFilesByClient(clientEmail)
    }

    fun deleteFile(fileId: Long): Flow<NetworkResultt<ApiMessage>> {
        return fileRepository.deleteFile(fileId)
    }
}