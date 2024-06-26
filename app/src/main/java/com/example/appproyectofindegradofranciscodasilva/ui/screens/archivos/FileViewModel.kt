package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.Balance
import com.example.appproyectofindegradofranciscodasilva.domain.services.BalanceService
import com.example.appproyectofindegradofranciscodasilva.domain.services.FileService
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
class FileViewModel @Inject constructor(
    private val fileService: FileService,
    private val balanceService: BalanceService
) : ViewModel() {

    private val _uiState = MutableStateFlow(FileState())
    val uiState: StateFlow<FileState> = _uiState.asStateFlow()


    fun handleEvent(event: FileEvent) {
        when (event) {
            is FileEvent.OnFileSelected -> _uiState.update {
                it.copy(
                    selectedFile = event.file
                )
            }

            is FileEvent.OnMimeTypeSelected -> _uiState.update {
                it.copy(
                    mimeType = event.mimeType
                )
            }

            is FileEvent.DownloadFile -> download(event.context, event.fileId)

            FileEvent.LoadAllFiles -> loadAllFiles()
            FileEvent.LoadIncomeFiles -> loadIncomeFiles()
            FileEvent.LoadExpenseFiles -> loadExpenseFiles()
            FileEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }

            is FileEvent.OnFilterChanged -> {

                if (!event.clientId.contains("@")) {
                    _uiState.update { it.copy(selectedFilter = event.filter) }
                    when (event.filter) {
                        FileFilter.Todos -> loadAllFiles()
                        FileFilter.Ingresos -> loadIncomeFiles()
                        FileFilter.Gastos -> loadExpenseFiles()
                    }
                } else {
                    _uiState.update { it.copy(selectedFilter = event.filter) }
                    when (event.filter) {
                        FileFilter.Todos -> loadAllFiles(event.clientId)
                        FileFilter.Ingresos -> loadIncomeFiles(event.clientId)
                        FileFilter.Gastos -> loadExpenseFiles(event.clientId)
                    }
                }

            }

            is FileEvent.OnTotalChange -> {
                _uiState.update {
                    it.copy(
                        total = event.total
                    )
                }
            }

            is FileEvent.OnIvaChange -> {
                _uiState.update {
                    it.copy(
                        iva = event.iva
                    )
                }
            }

            is FileEvent.UpdateFile -> update(
                event.balanceId,
                event.total,
                event.iva,
                event.clientId
            )

            is FileEvent.OnExpandedFileChange -> _uiState.update {
                it.copy(expandedFileId = event.fileId)
            }

            is FileEvent.LoadAllFilesByClientId -> loadAllFiles(event.clientId)

            is FileEvent.LoadIncomeFilesByClientId -> loadIncomeFiles(event.clientId)

            is FileEvent.LoadExpenseFilesByClientId -> loadExpenseFiles(event.clientId)
            is FileEvent.DeleteFile -> deleteFile(event.fileId, event.clientId)
        }
    }

    private fun deleteFile(fileId: Long, clientId: String) {
        viewModelScope.launch {
            fileService.deleteFile(fileId)
                .catch { cause ->
                    _uiState.update {
                        it.copy(message = cause.message, isLoading = false)
                    }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Success -> {
                            _uiState.update {
                                it.copy(message = result.data?.message)
                            }

                            if (!clientId.contains("@")) {
                                when (_uiState.value.selectedFilter) {
                                    FileFilter.Todos -> loadAllFiles()
                                    FileFilter.Ingresos -> loadIncomeFiles()
                                    FileFilter.Gastos -> loadExpenseFiles()
                                }
                            } else {
                                when (_uiState.value.selectedFilter) {
                                    FileFilter.Todos -> loadAllFiles(clientId)
                                    FileFilter.Ingresos -> loadIncomeFiles(clientId)
                                    FileFilter.Gastos -> loadExpenseFiles(clientId)
                                }
                            }

                        }

                        is NetworkResultt.Error -> _uiState.update {
                            it.copy(
                                message = result.message,
                                isLoading = false
                            )
                        }

                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }


    private fun update(balanceId: Long, total: String, iva: String, clientId: String) {
        if ((_uiState.value.iva.isEmpty() || _uiState.value.iva.toDoubleOrNull() == null) || (_uiState.value.total.isEmpty() || _uiState.value.total.toDoubleOrNull() == null)) {
            _uiState.update {
                it.copy(
                    message = "Verifique formato de cifras (Ej: 100.00)",
                )
            }
        } else {
            val balance: Balance =
                if (_uiState.value.selectedFilter == FileFilter.Ingresos) {
                    Balance(balanceId, total.toDouble(), 0.0, iva.toDouble(), null)
                } else {
                    Balance(balanceId, 0.0, total.toDouble(), iva.toDouble(), null)
                }

            viewModelScope.launch {
                balanceService.updateBalance(balance)
                    .catch(action = { cause ->
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
                                _uiState.update { it.copy(isLoading = true) }
                            }

                            is NetworkResultt.Success -> {
                                _uiState.update {
                                    it.copy(
                                        message = result.data?.message,
                                        isLoading = false,
                                        total = "",
                                        iva = ""
                                    )
                                }

                                if (!clientId.contains("@")) {
                                    when (_uiState.value.selectedFilter) {
                                        FileFilter.Todos -> loadAllFiles()
                                        FileFilter.Ingresos -> loadIncomeFiles()
                                        FileFilter.Gastos -> loadExpenseFiles()
                                    }
                                } else {
                                    when (_uiState.value.selectedFilter) {
                                        FileFilter.Todos -> loadAllFiles(clientId)
                                        FileFilter.Ingresos -> loadIncomeFiles(clientId)
                                        FileFilter.Gastos -> loadExpenseFiles(clientId)
                                    }
                                }
                            }
                        }
                    }
            }
        }


    }

    private fun download(context: Context, fileId: Long) {
        viewModelScope.launch {
            fileService.download(fileId, context)
                .catch(action = { cause ->

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

                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is NetworkResultt.Success -> {
                            _uiState.update {
                                it.copy(
                                    message = result.data,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadAllFiles() {
        viewModelScope.launch {
            fileService.getFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun loadIncomeFiles() {
        viewModelScope.launch {
            fileService.getIncomeFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun loadExpenseFiles() {
        viewModelScope.launch {
            fileService.getExpensesFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }


    private fun loadAllFiles(clientId: String) {
        viewModelScope.launch {
            fileService.getFilesByClient(clientId)
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun loadIncomeFiles(clientId: String) {
        viewModelScope.launch {
            fileService.getIncomeFilesByClient(clientId)
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun loadExpenseFiles(clientId: String) {
        viewModelScope.launch {
            fileService.getExpensesFilesByClient(clientId)
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
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
                                files = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
        }
    }

}