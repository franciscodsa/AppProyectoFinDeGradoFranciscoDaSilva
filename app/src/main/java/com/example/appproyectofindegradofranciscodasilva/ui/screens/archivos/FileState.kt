package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import java.io.File

data class FileState(
    val selectedFile: File? = null,
    val isLoading: Boolean = false,
)
