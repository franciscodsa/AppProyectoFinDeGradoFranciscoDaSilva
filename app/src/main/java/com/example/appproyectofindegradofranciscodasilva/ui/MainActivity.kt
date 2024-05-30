package com.example.appproyectofindegradofranciscodasilva.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.Navigation
import com.example.appproyectofindegradofranciscodasilva.ui.theme.AppProyectoFinDeGradoFranciscoDaSilvaTheme
import com.example.appproyectofindegradofranciscodasilva.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var lifecycleObserver: LifecycleEventObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                CoroutineScope(Dispatchers.IO).launch {
                    tokenManager.clearStoredData()
                }


                lifecycle.removeObserver(lifecycleObserver)
            }
        }
        lifecycle.addObserver(lifecycleObserver)

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

