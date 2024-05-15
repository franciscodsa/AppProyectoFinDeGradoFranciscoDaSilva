package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

val screens = listOf(
    Screens("registro", Icons.Filled.Apps, Icons.Outlined.Apps, "Resgistro"),

    Screens("login", Icons.Filled.Apps,  Icons.Outlined.Apps,"Log-in"),

    Screens("resumen", Icons.Filled.Home, Icons.Outlined.Home, "Resumen"),

    Screens("files", Icons.Filled.Home, Icons.Outlined.Home, "Files")
)


val screensAdmin = screens +
        Screens("contadores", Icons.Filled.Contacts, Icons.Outlined.Contacts, "Contadores") +
        Screens("clientes", Icons.Filled.Contacts,  Icons.Outlined.Contacts, "Clientes")

val screensContador = screens +
        Screens("clientes", Icons.Filled.Contacts, Icons.Outlined.Contacts, "Clientes")

val screensCliente = screens

data class Screens(val route: String, val iconoSeleccionado: ImageVector, val iconoDeseleccionado: ImageVector, val titulo : String )