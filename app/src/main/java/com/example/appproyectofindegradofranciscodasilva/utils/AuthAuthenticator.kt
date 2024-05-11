package com.example.appproyectofindegradofranciscodasilva.utils

import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.CredentialApiServices
import com.example.appproyectofindegradofranciscodasilva.data.model.LoginInfoResponse

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        val refreshToken = runBlocking {
            //obtiene el refresh del token manager
            tokenManager.getRefreshToken().first()
        }
        return if (refreshToken == null) {
            response.request.newBuilder().build()
        } else {
            runBlocking {
                //usa el refresh para obtener un nuevo access token
                val result = getNewAccessToken(refreshToken)
                val accessTokenRefreshed = result.data?.accessToken

                //lo guarda en el en el data store
                accessTokenRefreshed?.let { tokenManager.saveAccessToken(it) }

                //construye la llamada de nuevo colocando el nuevo access token en el header
                response.request.newBuilder().header(ConstantesUtils.authorization, "${ConstantesUtils.bearer} $accessTokenRefreshed")
                    .build()
            }
        }

    }

    private suspend fun getNewAccessToken(refreshToken: String): NetworkResultt<LoginInfoResponse> {
        try {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constantes.urlBaseLogin)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            val service = retrofit.create(CredentialApiServices::class.java)
            val response = service.refreshAccessToken(refreshToken)

            return if (response.isSuccessful) {
                val loginTokens = response.body()
                if (loginTokens != null) {
                    NetworkResultt.Success(loginTokens)
                } else {
                    NetworkResultt.Error("${response.code()} ${response.message()}")
                }
            } else {
                NetworkResultt.Error("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            return NetworkResultt.Error(e.message ?: e.toString())
        }

    }
}