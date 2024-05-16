package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import java.io.File

sealed class FileEvent {

    object UploadFile: FileEvent()

    class OnFileSelected(val file: File): FileEvent()

    class OnMimeTypeSelected(val mimeType : String): FileEvent()
}