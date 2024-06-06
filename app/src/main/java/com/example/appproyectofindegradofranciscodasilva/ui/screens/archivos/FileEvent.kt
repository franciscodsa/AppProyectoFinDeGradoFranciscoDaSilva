package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.content.Context
import com.example.appproyectofindegradofranciscodasilva.ui.screens.clients.ClientEvent
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterEvent
import java.io.File

sealed class FileEvent {

    class UpdateFile (val balanceId: Long, val total: String, val iva: String, val clientId: String ): FileEvent()

    class DownloadFile (val context: Context, val fileId: Long): FileEvent()

    class OnFileSelected(val file: File): FileEvent()

    class OnMimeTypeSelected(val mimeType : String): FileEvent()

    class OnExpandedFileChange(val fileId: Long?) : FileEvent()

    class LoadAllFilesByClientId(val clientId: String) : FileEvent()

    class LoadIncomeFilesByClientId(val clientId: String) : FileEvent()

    class LoadExpenseFilesByClientId(val clientId: String) : FileEvent()

    object LoadAllFiles : FileEvent()

    object LoadIncomeFiles : FileEvent()

    object LoadExpenseFiles : FileEvent()

    object MessageSeen : FileEvent()

    class OnFilterChanged(val filter: FileFilter, val clientId: String) : FileEvent()

    class OnTotalChange(val total: String): FileEvent()

    class OnIvaChange (val iva: String): FileEvent()

    class DeleteFile(val fileId: Long, val clientId: String) : FileEvent()

}