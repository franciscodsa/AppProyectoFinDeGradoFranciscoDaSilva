package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos.FileEvent
import java.io.File

sealed class ResumenEvent {
    object SetUserRole: ResumenEvent()

    object UploadFile: ResumenEvent()
    class OnFileSelected(val file: File): ResumenEvent()
    class OnMimeTypeSelected(val mimeType : String): ResumenEvent()

    class OnNewInvoiceTotal (val total : String): ResumenEvent()
    class OnNewInvoiceIva (val iva : String): ResumenEvent()
    class OnNewInvoiceDescription (val description : String): ResumenEvent()
    object OnInvoiceTypeSelected : ResumenEvent()

    class OnTrimesterSelected(val quarter: String) : ResumenEvent()
    class OnYearSelected(val year: String) : ResumenEvent()
    object OnTrimesterMenuExpandedChanged : ResumenEvent()
    object OnYearMenuExpandedChanged : ResumenEvent()
    object MessageSeen : ResumenEvent()

}