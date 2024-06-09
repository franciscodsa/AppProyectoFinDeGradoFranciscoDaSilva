package com.example.appproyectofindegradofranciscodasilva.data.source.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.AccountantApiService
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.BalanceApiService
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.ClientApiService
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.CredentialApiService
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.FileApiService
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.UserApiService
import com.example.appproyectofindegradofranciscodasilva.utils.AuthAuthenticator
import com.example.appproyectofindegradofranciscodasilva.utils.AuthInterceptor
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Named
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constantes.dataStore)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)


    // Proporciona FirebaseAuth
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Proporciona FirebaseFirestore
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    //Proporciona Moshi
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(LocalDateTimeAdapter())
            .build()
    }


    // Proporciona MoshiConverterFactory
    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(LocalDateTimeAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }


    // Proporciona OkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    // Proporciona una instancia de Retrofit para CredentialApiService
    @Named("CredentialServer")
    @Singleton
    @Provides
    fun provideCredentialRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constantes.urlBaseLogin)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    // Proporciona una instancia de Retrofit para el resto
    @Named("InfoServer")
    @Singleton
    @Provides
    fun provideClientRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constantes.urlBaseInfo)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    // Proporciona CredentialApiService utilizando la instancia de Retrofit correspondiente
    @Singleton
    @Provides
    fun provideCredentialService(@Named("CredentialServer") retrofit: Retrofit): CredentialApiService =
        retrofit.create(CredentialApiService::class.java)

    // Proporciona ClientApiService utilizando la instancia de Retrofit correspondiente
    @Singleton
    @Provides
    fun provideClientService(@Named("InfoServer") retrofit: Retrofit): ClientApiService =
        retrofit.create(ClientApiService::class.java)

    @Singleton
    @Provides
    fun provideAccountantService(@Named("InfoServer") retrofit: Retrofit): AccountantApiService =
        retrofit.create(AccountantApiService::class.java)

    @Singleton
    @Provides
    fun provideFileService(@Named("InfoServer") retrofit: Retrofit): FileApiService =
        retrofit.create(FileApiService::class.java)

    @Singleton
    @Provides
    fun provideBalanceService(@Named("InfoServer") retrofit: Retrofit): BalanceApiService =
        retrofit.create(BalanceApiService::class.java)

    @Singleton
    @Provides
    fun provideUserService(@Named("InfoServer") retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)
}

class LocalDateTimeAdapter {

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

class LocalDateAdapter {
    @ToJson
    fun toJson( value: LocalDate): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}