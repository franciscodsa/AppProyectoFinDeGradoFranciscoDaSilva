package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            is FileEvent.DownloadFile -> download(event.context)
            is FileEvent.OnFileIdChange -> _uiState.update {
                it.copy(
                    fileId = event.fileId
                )
            }
        }
    }

    private fun download(context: Context) {
        viewModelScope.launch {
            fileServices.download(_uiState.value.fileId.toLong(), context)
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
                    "add4@mail.com"
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

}