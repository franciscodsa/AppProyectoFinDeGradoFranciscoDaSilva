package com.example.appproyectofindegradofranciscodasilva.data.source

import android.util.Log
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.model.ApiMessage
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.ClientApiServices
import com.example.appproyectofindegradofranciscodasilva.data.source.di.InfoServer
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import javax.inject.Inject

class ClientDataSource @Inject constructor(
    private val clientApiServices: ClientApiServices,
   /* @InfoServer private val retrofit: Retrofit,*/
    private val moshi: Moshi
) {

   /* private val clientApiServices: ClientApiServices = retrofit.create(ClientApiServices::class.java)*/


    suspend fun addClient(client: Client): NetworkResultt<Client> {
        try {
            Log.i("asdasd", "AAAAAAAAAAAAAAAAAAAAAAA")

            val response = clientApiServices.addClient(client)

            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string() ?: Constantes.unknownError
                val adapter = moshi.adapter(ApiMessage::class.java)
                val apiMessage = adapter.fromJson(errorMessage)
                return NetworkResultt.Error(apiMessage?.message ?: Constantes.unknownError)
            } else {
                val body = response.body()

                body?.let {
                    return NetworkResultt.Success(it)
                }
                error(Constantes.noData)
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(Constantes.unknownError)
        }
    }
}

