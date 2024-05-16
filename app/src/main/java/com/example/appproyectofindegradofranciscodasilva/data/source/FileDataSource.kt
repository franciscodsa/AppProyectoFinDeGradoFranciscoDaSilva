package com.example.appproyectofindegradofranciscodasilva.data.source

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.FileApiServices
import com.example.appproyectofindegradofranciscodasilva.data.source.di.InfoServer
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.File
import javax.inject.Inject

class FileDataSource @Inject constructor(
    @InfoServer private val retrofit: Retrofit,
    private val moshi: Moshi
) {
    private val fileApiServices: FileApiServices = retrofit.create(FileApiServices::class.java)

    suspend fun upload(file: File, mimeType: String, description: String, clientEmail: String) : NetworkResultt<ApiMessage>{
        try {
            Log.i("data", file.name)

            val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailPart = clientEmail.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = fileApiServices.uploadFile(filePart, descriptionPart, emailPart)

            if (!response.isSuccessful){

                Log.i("dataFailed", file.name)
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            }else{
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        }catch (e: Exception) {
            Log.e("error", e.message.toString())
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }
//todo: rcuerda eliminar esto

//    suspend fun uploadFile(uri: Uri, description: String, clientEmail: String) {
//        val context = LocalContext.current
//        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
//        val file = File(parcelFileDescriptor?.fileDescriptor)
//        val requestFile = file.asRequestBody("*/*".toMediaTypeOrNull())
//        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
//        val emailPart = clientEmail.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://your-api-url.com/")
//            .build()
//
//        val service = retrofit.create(ApiService::class.java)
//        val response = service.uploadFile(body, descriptionPart, emailPart)
//
//        if (response.isSuccessful) {
//            // Handle success
//        } else {
//            // Handle error
//        }
//    }

}


