package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterEvent
import java.io.File

sealed class FileEvent {

    object UploadFile: FileEvent()

   /* class DownloadFile (val context: Context): FileEvent()*/
    class DownloadFile (val context: Context, val fileId: Long): FileEvent()

    class OnFileSelected(val file: File): FileEvent()

    class OnMimeTypeSelected(val mimeType : String): FileEvent()

    class OnFileIdChange(val fileId: String): FileEvent()

    object LoadAllFiles : FileEvent()
    object LoadIncomeFiles : FileEvent()
    object LoadExpenseFiles : FileEvent()

    object MessageSeen : FileEvent()

}