package com.example.appproyectofindegradofranciscodasilva.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.Drawer
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.Navigation
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginScreen
import com.example.appproyectofindegradofranciscodasilva.ui.theme.AppProyectoFinDeGradoFranciscoDaSilvaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            AppProyectoFinDeGradoFranciscoDaSilvaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

