package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos.FilesScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.clients.ClientScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores.AccountantScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave.OlvideClaveScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.profile.ProfileScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterScreen
import com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen.ResumenScreen


const val login = "login"

@Composable
fun Navigation(
) {
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
                    navController.navigate("resumen")
                },
                toRegistroScreen = {
                    navController.navigate("registro")
                },
                toClaveOlvidadaScreen = {
                    navController.navigate("olvide")
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screens
                    )
                }
            )
        }
        composable(
            "registro"
        ) {
            RegisterScreen(

            )
        }

        composable(
            "olvide"
        ) {
            OlvideClaveScreen(
            )
        }

        composable(
            route = "resumen",
        ) {
            ResumenScreen(
                onClientsClick = { navController.navigate("clients") },
                onAccountansClick ={ navController.navigate("accountants")},
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screens
                    )
                })
        }

        composable(
            "files"
        ) {
            FilesScreen(
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screens
                    )
                }
            )
        }

        composable(
            "profile"
        ) {
            ProfileScreen(
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screens
                    )
                }
            )
        }

        composable(
            "clients"
        ) {
            ClientScreen(onChatClick = {}, onFilesClick = {})
        }

        composable(
            "accountants"
        ) {
            AccountantScreen(onAddClick = {})
        }

    }
}