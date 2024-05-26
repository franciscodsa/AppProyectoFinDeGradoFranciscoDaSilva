package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.User
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialServices
import com.example.appproyectofindegradofranciscodasilva.domain.services.UserServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userService: UserServices,
    private val credentialServices: CredentialServices
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()



    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnFirstNameChanged -> _uiState.update { it.copy(firstName = event.firstName) }
            is ProfileEvent.OnLastNameChanged -> _uiState.update { it.copy(lastName = event.lastName) }
            is ProfileEvent.OnPhoneChanged -> _uiState.update { it.copy(phone = event.phone) }
            is ProfileEvent.OnYearFieldChange -> _uiState.update { it.copy(year = event.year) }
            is ProfileEvent.OnMonthFieldChange -> _uiState.update { it.copy(month = event.month) }
            is ProfileEvent.OnDayFieldChange -> _uiState.update { it.copy(day = event.day) }
            ProfileEvent.SaveChanges -> saveChanges()
            ProfileEvent.Logout -> logout()
            ProfileEvent.MessageSeen -> _uiState.update { it.copy(message = null) }
            ProfileEvent.LoadUserData -> getUserProfile()
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            userService.getUserByEmail()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update {
                            val dateOfBirth = result.data?.dateOfBirth.toString().split("-")
                            it.copy(
                                email = result.data?.email?: "",
                                firstName = result.data?.firstName?: "",
                                lastName = result.data?.lastName?: "",
                                phone = result.data?.phone?: "",
                                year = dateOfBirth[0],
                                month = dateOfBirth[1],
                                day = dateOfBirth[2],
                                isLoadin = false
                            )
                        }
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message) }
                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoadin = true) }
                    }
                }
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            val updatedUser = _uiState.value.let {
                User(
                    email = it.email,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    phone = it.phone,
                    dateOfBirth = LocalDate.of(it.year.toInt(),it.month.toInt(), it.day.toInt())
                )
            }
            userService.updateUser(updatedUser)
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoadin = false) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update { it.copy(message = result.data?.message, isLoadin = false) }
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message, isLoadin = false) }
                        is NetworkResultt.Loading -> _uiState.update { it.copy( isLoadin = true) }
                    }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            credentialServices.logout()
        }
    }
}
