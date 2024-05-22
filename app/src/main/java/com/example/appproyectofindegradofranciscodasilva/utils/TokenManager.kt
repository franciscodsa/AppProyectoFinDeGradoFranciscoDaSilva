package com.example.appproyectofindegradofranciscodasilva.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.appproyectofindegradofranciscodasilva.data.source.di.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private val accessTokenKey = stringPreferencesKey(ConstantesUtils.jwtToken)
        private val refreshTokenKey = stringPreferencesKey(ConstantesUtils.refreshJwt)
        private val currentUser = stringPreferencesKey(ConstantesUtils.currentUser)
        private val role = stringPreferencesKey(ConstantesUtils.role)
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[accessTokenKey]
        }
    }


    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }


    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[refreshTokenKey]
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[refreshTokenKey] = token
        }
    }

    fun getCurrentUser(): Flow<String?>{
        return context.dataStore.data.map{ preferences ->
            preferences[currentUser]
        }
    }

    suspend fun saveCurrentUser(email: String){
        context.dataStore.edit { preferences ->
            preferences[currentUser] = email
        }
    }

    fun getRole(): Flow<String?>{
        return context.dataStore.data.map{ preferences ->
            preferences[role]
        }
    }

    suspend fun saveRole(userRole: String){
        context.dataStore.edit { preferences ->
            preferences[role] = userRole
        }
    }

    suspend fun clearStoredData() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(currentUser)
            preferences.remove(role)
        }
    }
}