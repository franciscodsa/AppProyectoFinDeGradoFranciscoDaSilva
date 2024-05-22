package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    /*private val userService: UserService*/
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        getUserProfile()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnFirstNameChanged -> _uiState.update { it.copy(firstName = event.firstName) }
            is ProfileEvent.OnLastNameChanged -> _uiState.update { it.copy(lastName = event.lastName) }
            is ProfileEvent.OnPhoneChanged -> _uiState.update { it.copy(phone = event.phone) }
            is ProfileEvent.OnDateOfBirthChanged -> _uiState.update { it.copy(dateOfBirth = event.dateOfBirth) }
            ProfileEvent.SaveChanges -> saveChanges()
            ProfileEvent.Logout -> logout()
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
           /* userService.getUserProfile()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> _uiState.update {
                            it.copy(
                                email = result.data.email,
                                firstName = result.data.firstName,
                                lastName = result.data.lastName,
                                phone = result.data.phone,
                                dateOfBirth = result.data.dateOfBirth.toString()
                            )
                        }
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message) }
                        is NetworkResultt.Loading -> {} // Handle loading state if needed
                    }
                }*/
        }
    }

    private fun saveChanges() {
        // Implement save changes logic
    }

    private fun logout() {
        // Implement logout logic
    }
}
