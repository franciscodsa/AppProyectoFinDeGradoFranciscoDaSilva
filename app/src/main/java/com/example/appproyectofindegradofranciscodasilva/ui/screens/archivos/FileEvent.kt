package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import java.io.File

sealed class FileEvent {

    object UploadFile: FileEvent()

    class DownloadFile (val context: Context): FileEvent()

    class OnFileSelected(val file: File): FileEvent()

    class OnMimeTypeSelected(val mimeType : String): FileEvent()

    class OnFileIdChange(val fileId: String): FileEvent()
}