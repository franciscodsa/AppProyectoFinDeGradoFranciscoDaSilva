package com.example.appproyectofindegradofranciscodasilva.data.model

import java.time.LocalDateTime


data class FileInfo(
    val id: Long,
    val fileName: String,
    val description: String,
    val date: LocalDateTime,
    val balanceId: Long,
    val total: Double,
    val iva: Double
)