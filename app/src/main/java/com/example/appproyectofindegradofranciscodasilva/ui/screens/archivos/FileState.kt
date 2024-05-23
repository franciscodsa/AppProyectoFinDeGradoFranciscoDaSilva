package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import com.example.appproyectofindegradofranciscodasilva.data.model.FilesInfo
import java.io.File

data class FileState(
    val selectedFile: File? = null,
    val mimeType: String = "",
    val isLoading: Boolean = false,
    //todo,creo no lo vas a necesitar, pero revisa
    val fileId: String = "",

    val files: List<FilesInfo> = emptyList(),
    val message: String? = null,
    val selectedFilter: FileFilter = FileFilter.Todos,

    val total: String = "",
    val iva: String = ""
)

enum class FileFilter {
    Todos, Ingresos, Gastos
}