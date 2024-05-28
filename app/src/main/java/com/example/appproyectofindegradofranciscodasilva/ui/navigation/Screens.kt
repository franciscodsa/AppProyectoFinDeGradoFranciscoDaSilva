package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

val screens = listOf(
    Screens(
        "registro",
        Icons.Filled.Apps,
        Icons.Outlined.Apps,
        "Registro"
    ),

    Screens(
        "login",
        Icons.Filled.Apps,
        Icons.Outlined.Apps,
        "Log-in"
    ),

    Screens(
        "resumen",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "Resumen"
    ),

    Screens(
        "files/{clientId}",
        Icons.Filled.Folder,
        Icons.Outlined.Folder,
        "Archivos"
    ),
    Screens(
        "profile",
        Icons.Filled.Person,
        Icons.Outlined.Person,
        "Perfil"
    )
)

data class Screens(
    val route: String,
    val iconoSeleccionado: ImageVector,
    val iconoDeseleccionado: ImageVector,
    val titulo: String
)