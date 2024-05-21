package com.example.appproyectofindegradofranciscodasilva.data.model

import java.time.LocalDateTime


data class FilesInfo (
     val id: Long,
     val fileName: String,
     val description: String,
     val date: LocalDateTime
)