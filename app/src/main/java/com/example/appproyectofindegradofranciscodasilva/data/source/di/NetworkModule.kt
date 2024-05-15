package com.example.appproyectofindegradofranciscodasilva.data.source.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.appproyectofindegradofranciscodasilva.common.Constantes
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.ClientApiServices
import com.example.appproyectofindegradofranciscodasilva.data.source.apiservices.CredentialApiServices
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
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
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constantes.dataStore)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CredentialServer

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InfoServer

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

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
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Proporciona una instancia de Retrofit para CredentialApiServices
    @CredentialServer
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
    @InfoServer
    @Singleton
    @Provides
    fun provideClientRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constantes.urlBaseInfo)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    // Proporciona CredentialApiServices utilizando la instancia de Retrofit correspondiente
    @Singleton
    @Provides
    fun provideCredentialService(@CredentialServer retrofit: Retrofit): CredentialApiServices =
        retrofit.create(CredentialApiServices::class.java)

    // Proporciona ClientApiServices utilizando la instancia de Retrofit correspondiente
    /*@Singleton
    @Provides
    fun provideClientService(@InfoServer retrofit: Retrofit): ClientApiServices =
        retrofit.create(ClientApiServices::class.java)*/
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