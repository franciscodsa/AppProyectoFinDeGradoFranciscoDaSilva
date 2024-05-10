package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave.OlvideClaveScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen.ResumenScreen


const val login = "login"

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
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
                },
                toRegistroScreen={
                    navController.navigate("registro")
                },
                toClaveOlvidadaScreen={
                    navController.navigate("olvide")
                },

            )
        }
        composable(
            "registro"
        ){
            RegisterScreen(innerPadding = innerPadding)
        }

        composable(
            "resumen"
        ){
            ResumenScreen()
        }

        composable(
            "olvide"
        ){
            OlvideClaveScreen()
        }

    }
}