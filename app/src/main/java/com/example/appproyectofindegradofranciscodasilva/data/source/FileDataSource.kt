package com.example.appproyectofindegradofranciscodasilva.data.source

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.FilesInfo
import com.example.appproyectofindegradofranciscodasilva.data.model.InvoiceType
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.FileApiServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class FileDataSource @Inject constructor(
    /*@InfoServer private val retrofit: Retrofit,*/
    private val fileApiServices: FileApiServices,
    private val moshi: Moshi
) {
   /* private val fileApiServices: FileApiServices = retrofit.create(FileApiServices::class.java)*/

    suspend fun upload(file: File, mimeType: String, description: String, clientEmail: String, invoiceType: InvoiceType, balance: Balance) : NetworkResultt<ApiMessage>{
        try {
            Log.i("data", file.name)

            val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailPart = clientEmail.toRequestBody("text/plain".toMediaTypeOrNull())
            val invoiceTypePart = invoiceType.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val incomePart = balance.income.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val expensesPart = balance.expenses.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val ivaPart = balance.iva.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val response = fileApiServices.uploadFile(filePart, descriptionPart, emailPart, invoiceTypePart, incomePart, expensesPart, ivaPart)

            if (!response.isSuccessful){

                Log.i("dataFailed", file.name)
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            }else{
                val body = response.body()

                body?.let {
                    Log.i("d", it.message)
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)

            }
        }catch (e: Exception) {
            Log.e("error", e.message.toString())
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }


    suspend fun download(fileId: Long, context : Context): NetworkResultt<String>{
        try {
            val response = fileApiServices.downloadFile(fileId)

            if (!response.isSuccessful){
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            }else {
                try {
                    // Verifica si la solicitud fue exitosa
                    if (response.isSuccessful) {
                        val contentType = response.headers()["Content-Type"]
                        val contentDisposition = response.headers()["Content-Disposition"]
                        val inputStream = response.body()?.byteStream()

                        if (contentType != null && inputStream != null) {
                            saveDownloadedFile(context, contentType, contentDisposition, inputStream)
                            return NetworkResultt.Success("Descarga exitosa")
                        } else {
                            return NetworkResultt.Error("No se pudo obtener el tipo MIME o el flujo de datos")
                        }
                    } else {
                        return NetworkResultt.Error("Error en la solicitud: ${response.code()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return NetworkResultt.Error("Error durante la descarga: ${e.message}")
                }

            }
        }catch (e: Exception) {
            Log.e("error", e.message.toString())
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }

    suspend fun getFilesByClient(clientEmail: String): NetworkResultt<List<FilesInfo>> {
        return try {
            val response = fileApiServices.getFilesByClient(clientEmail)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { NetworkResultt.Success(it) } ?: NetworkResultt.Error("No data")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                NetworkResultt.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResultt.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getExpensesFilesByClient(clientEmail: String): NetworkResultt<List<FilesInfo>> {
        return try {
            val response = fileApiServices.getExpensesFilesByClient(clientEmail)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { NetworkResultt.Success(it) } ?: NetworkResultt.Error("No data")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                NetworkResultt.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResultt.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getIncomeFilesByClient(clientEmail: String): NetworkResultt<List<FilesInfo>> {
        return try {
            val response = fileApiServices.getIncomeFilesByClient(clientEmail)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { NetworkResultt.Success(it) } ?: NetworkResultt.Error("No data")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                NetworkResultt.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResultt.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteFile(fileId: Long): NetworkResultt<ApiMessage> {
        return try {
            val response = fileApiServices.deleteFile(fileId)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { NetworkResultt.Success(it) } ?: NetworkResultt.Error("No data")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                NetworkResultt.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResultt.Error(e.message ?: "Unknown error")
        }
    }



    private fun saveDownloadedFile(
        context: Context,
        contentType: String,
        contentDisposition: String?,
        stream: InputStream
    ) {
        try {
            val fileName = extractFileName(contentDisposition) ?: generateFileName(contentType)

            Log.i("DEBUG", fileName)

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
               /* put(MediaStore.MediaColumns.MIME_TYPE, contentType)*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
            }

            val resolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    try {
                        stream.use { input ->
                            resolver.openOutputStream(uri).use { output ->
                                input.copyTo(output!!)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {

                Log.i("DEBUG", "else")

                val target = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                FileOutputStream(target).use { output ->
                    stream.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun extractFileName(contentDisposition: String?): String? {
        if (contentDisposition != null) {
            val startIndex = contentDisposition.indexOf("filename=")
            if (startIndex != -1) {
                var endIndex = contentDisposition.indexOf(";", startIndex)
                if (endIndex == -1) {
                    endIndex = contentDisposition.length
                }
                var fileName = contentDisposition.substring(startIndex + 9, endIndex)
                // Elimina las comillas del nombre del archivo, si están presentes
                fileName = fileName.replace("\"", "")
                // Elimina espacios en blanco adicionales alrededor del nombre del archivo
                fileName = fileName.trim()
                return fileName
            }
        }
        return null
    }

    private fun generateFileName(contentType: String): String {
        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.ENGLISH).format(System.currentTimeMillis()) + getExtensionFromMimeType(contentType)
    }

    private fun getExtensionFromMimeType(contentType: String): String {
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType)
        return if (!extension.isNullOrBlank()) {
            ".$extension"
        } else {
            ""
        }
    }




}


