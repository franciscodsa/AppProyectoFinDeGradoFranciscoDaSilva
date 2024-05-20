package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.InvoiceType
import com.example.appproyectofindegradofranciscodasilva.domain.services.BalanceServices
import com.example.appproyectofindegradofranciscodasilva.domain.services.FileServices
import com.example.appproyectofindegradofranciscodasilva.utils.NetworkResultt
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
class ResumeViewModel @Inject constructor(
    private val balanceServices: BalanceServices,
    private val fileServices: FileServices
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumenState())
    val uiState: StateFlow<ResumenState> = _uiState.asStateFlow()

    init {
        val currentYear = LocalDate.now().year
        val years = (currentYear downTo currentYear - 3).map { it.toString() }
        val currentMonth = LocalDate.now().monthValue
        val currentTrimester = when (currentMonth) {
            in 1..3 -> "T1"
            in 4..6 -> "T2"
            in 7..9 -> "T3"
            else -> "T4"
        }

        _uiState.value = ResumenState(
            selectedTrimester = currentTrimester,
            selectedYear = currentYear.toString(),
            years = years
        )

        getBalance()
    }


    fun handleEvent(event: ResumenEvent) {
        when (event) {
            ResumenEvent.OnTrimesterMenuExpandedChanged -> _uiState.update {
                it.copy(
                    expandedTrimestre = !_uiState.value.expandedTrimestre
                )
            }

            is ResumenEvent.OnTrimesterSelected -> {
                _uiState.update {
                    it.copy(
                        selectedTrimester = event.quarter,
                        expandedTrimestre = false
                    )
                }
                getBalance()
            }

            ResumenEvent.OnYearMenuExpandedChanged -> _uiState.update {
                it.copy(
                    expandedYear = !_uiState.value.expandedYear
                )
            }

            is ResumenEvent.OnYearSelected -> {
                _uiState.update {
                    it.copy(
                        selectedYear = event.year,
                        expandedYear = false
                    )
                }
                getBalance()
            }

            ResumenEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }

            is ResumenEvent.OnFileSelected ->  _uiState.update {
                it.copy(
                    selectedFile = event.file
                )
            }

            is ResumenEvent.OnMimeTypeSelected -> _uiState.update {
                it.copy(
                    mimeType = event.mimeType
                )
            }
            ResumenEvent.UploadFile -> upload()
        }
    }

    private fun upload() {
        if (_uiState.value.selectedFile == null) {
            Log.i("f", "file es null")
        } else {

            Log.i("f", _uiState.value.selectedFile?.name.toString())

            viewModelScope.launch {
                fileServices.upload(
                    _uiState.value.selectedFile!!,
                    _uiState.value.mimeType,
                    "test",
                    "add4@mail.com",
                    InvoiceType.INCOME
                ).catch(action = { cause ->
                    Log.i("f", "error al catch")
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }).collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> {
                            Log.i("f", "error en when")
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }

                        is NetworkResultt.Loading -> {
                            Log.i("f", "loading")
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is NetworkResultt.Success -> {
                            _uiState.update {
                                it.copy(
                                    message = result.data?.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBalance() {
        //todo: aqui se tendria que realizar la llamada de obtener el balance para el trimestre y año seleccionado y actualizar el state

        viewModelScope.launch {
            Log.i("ASD", "Entro")
            balanceServices.getQuarterBalance(
                _uiState.value.selectedYear.toInt(),
                _uiState.value.selectedTrimester
            ).catch(action = { cause ->
                _uiState.update {
                    it.copy(
                        message = cause.message,
                        isLoading = false
                    )
                }
            }).collect { result ->
                when (result) {
                    is NetworkResultt.Error -> _uiState.update {
                        it.copy(
                            message = result.message,
                            isLoading = false
                        )
                    }

                    is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is NetworkResultt.Success -> _uiState.update {
                        it.copy(
                            income = result.data?.income ?: 0.0,
                            expenses = result.data?.expenses ?: 0.0,
                            irpf = result.data?.irpf ?: 0.0,
                            iva = result.data?.iva ?: 0.0,
                            isLoading = false
                        )
                    }
                }

            }
        }
    }
}