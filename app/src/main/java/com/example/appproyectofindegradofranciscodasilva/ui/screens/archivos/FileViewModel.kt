package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appproyectofindegradofranciscodasilva.data.model.InvoiceType
import com.example.appproyectofindegradofranciscodasilva.domain.services.FileServices
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
    private val fileServices: FileServices
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

            FileEvent.UploadFile -> upload()
            is FileEvent.OnMimeTypeSelected -> _uiState.update {
                it.copy(
                    mimeType = event.mimeType
                )
            }

            is FileEvent.DownloadFile -> download(event.context, event.fileId)
            is FileEvent.OnFileIdChange -> _uiState.update {
                it.copy(
                    fileId = event.fileId
                )
            }

            FileEvent.LoadAllFiles -> loadAllFiles()
            FileEvent.LoadIncomeFiles -> loadIncomeFiles()
            FileEvent.LoadExpenseFiles -> loadExpenseFiles()
            FileEvent.MessageSeen -> _uiState.update {
                it.copy(
                    message = null
                )
            }
        }
    }

    private fun download(context: Context, fileId : Long) {
        viewModelScope.launch {
            fileServices.download(fileId, context)
                .catch(action = { cause ->
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
                                message = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
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
                            Log.i("f", "subido")
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadAllFiles() {
        viewModelScope.launch {
            fileServices.getFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message, isLoading = false) }
                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResultt.Success -> _uiState.update { it.copy(files = result.data ?: emptyList(), isLoading = false) }
                    }
                }
        }
    }

    private fun loadIncomeFiles() {
        viewModelScope.launch {
            fileServices.getIncomeFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message, isLoading = false) }
                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResultt.Success -> _uiState.update { it.copy(files = result.data ?: emptyList(), isLoading = false) }
                    }
                }
        }
    }

    private fun loadExpenseFiles() {
        viewModelScope.launch {
            fileServices.getExpensesFilesByClient()
                .catch { cause ->
                    _uiState.update { it.copy(message = cause.message, isLoading = false) }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResultt.Error -> _uiState.update { it.copy(message = result.message, isLoading = false) }
                        is NetworkResultt.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResultt.Success -> _uiState.update { it.copy(files = result.data ?: emptyList(), isLoading = false) }
                    }
                }
        }
    }

}