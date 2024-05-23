package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterEvent
import java.io.File

sealed class FileEvent {

    class UpdateFile (val balanceId: Long, val total: String, val iva: String ): FileEvent()

    class DownloadFile (val context: Context, val fileId: Long): FileEvent()

    class OnFileSelected(val file: File): FileEvent()

    class OnMimeTypeSelected(val mimeType : String): FileEvent()

    class OnFileIdChange(val fileId: String): FileEvent()

    object LoadAllFiles : FileEvent()
    object LoadIncomeFiles : FileEvent()
    object LoadExpenseFiles : FileEvent()

    object MessageSeen : FileEvent()

    class OnFilterChanged(val filter: FileFilter) : FileEvent()

    class OnTotalChange(val total: String): FileEvent()

    class OnIvaChange (val iva: String): FileEvent()

}