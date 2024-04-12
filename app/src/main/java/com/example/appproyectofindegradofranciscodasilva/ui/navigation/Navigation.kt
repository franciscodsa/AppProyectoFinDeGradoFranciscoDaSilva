package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginScreen


const val login = "login"

@Composable
fun Navigation() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = login
    ) {
        composable(
            login
        ) {
            LoginScreen(
                onLogin = {
                    /*
                    usar para navegar a pantalla de bienvenida
                     navController.navigate()
                     */
                }
            )
        }
    }
}