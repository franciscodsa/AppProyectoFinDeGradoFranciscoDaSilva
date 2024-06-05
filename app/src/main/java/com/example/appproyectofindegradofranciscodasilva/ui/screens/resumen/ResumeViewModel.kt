package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.data.model.InvoiceType
import com.example.appproyectofindegradofranciscodasilva.domain.services.BalanceServices
import com.example.appproyectofindegradofranciscodasilva.domain.services.CredentialServices
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
    private val credentialServices: CredentialServices,
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

            is ResumenEvent.OnNewInvoiceDescription -> _uiState.update {
                it.copy(
                    newInvoiceDescription = event.description
                )
            }

            is ResumenEvent.OnNewInvoiceIva -> _uiState.update {
                it.copy(
                    newInvoiceIva = event.iva
                )
            }

            is ResumenEvent.OnNewInvoiceTotal -> _uiState.update {
                it.copy(
                    newInvoiceTotal = event.total
                )
            }

            ResumenEvent.OnInvoiceTypeSelected -> _uiState.update {
                it.copy(
                    isExpense = !_uiState.value.isExpense
                )
            }

            is ResumenEvent.SetUserRole ->  setRole()

        }
    }

    private fun setRole() {
        viewModelScope.launch {
            val role = credentialServices.getRole()

            _uiState.update {
                it.copy(
                    userRole = role
                )
            }

        }
    }

    private fun upload() {
        Log.i("isExpense", _uiState.value.isExpense.toString())
        if (_uiState.value.selectedFile == null || _uiState.value.newInvoiceDescription.isEmpty() || (_uiState.value.newInvoiceIva.isEmpty() || _uiState.value.newInvoiceIva.toDoubleOrNull() == null )||  (_uiState.value.newInvoiceTotal.isEmpty() || _uiState.value.newInvoiceTotal.toDoubleOrNull() == null )) {
            _uiState.update {
                it.copy(
                    message = "Seleccione archivo y verifique formato de cifras (Ej: 100.00)",
                )
            }

        } else {

            val invoiceType = if (_uiState.value.isExpense) InvoiceType.EXPENSE else InvoiceType.INCOME
            val balance = Balance(
                id= 0,
                income = if (invoiceType == InvoiceType.INCOME) _uiState.value.newInvoiceTotal.toDouble() else 0.0,
                expenses = if (invoiceType == InvoiceType.EXPENSE) _uiState.value.newInvoiceTotal.toDouble() else 0.0,
                iva = _uiState.value.newInvoiceIva.toDouble(),
                clientEmail = null
            )

            viewModelScope.launch {
                fileServices.upload(
                    _uiState.value.selectedFile!!,
                    _uiState.value.mimeType,
                    _uiState.value.newInvoiceDescription,
                    invoiceType,
                    balance

                ).catch(action = { cause ->

                    _uiState.update {
                        it.copy(
                            message = cause.message,
                            isLoading = false
                        )
                    }
                }).collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> {

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
                                    message = "Archivo subido y balance agregado exitosamente.",
                                    isLoading = false,
                                    newInvoiceTotal = "",
                                    newInvoiceIva = "",
                                    newInvoiceDescription = "",
                                    selectedFile = null,
                                    mimeType = ""
                                )
                            }

                            getBalance()
                         /*   val balance = Balance(
                                income = if (invoiceType == InvoiceType.INCOME) _uiState.value.newInvoiceTotal.toDouble() else 0.0,
                                expenses = if (invoiceType == InvoiceType.EXPENSE) _uiState.value.newInvoiceTotal.toDouble() else 0.0,
                                iva = _uiState.value.newInvoiceIva.toDouble(),
                                clientEmail = null
                            )

                            addBalance(balance)*/
                        }
                    }
                }
            }
        }
    }


   /* private fun addBalance(balance: Balance) {
        viewModelScope.launch {
            balanceServices.updateBalance(balance).catch { cause ->
                _uiState.update {
                    it.copy(
                        message = cause.message,
                        isLoading = false
                    )
                }
            }.collect { result ->
                when (result) {
                    is NetworkResultt.Error -> {
                        _uiState.update {
                            it.copy(
                                message = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is NetworkResultt.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is NetworkResultt.Success -> {
                        _uiState.update {
                            it.copy(
                                message = "Archivo subido y balance agregado exitosamente.",
                                isLoading = false,
                                newInvoiceTotal = "",
                                newInvoiceIva = "",
                                newInvoiceDescription = "",
                                selectedFile = null,
                                mimeType = ""
                            )
                        }

                        getBalance()
                    }
                }
            }
        }
    }
*/


    private fun getBalance() {
        viewModelScope.launch {

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