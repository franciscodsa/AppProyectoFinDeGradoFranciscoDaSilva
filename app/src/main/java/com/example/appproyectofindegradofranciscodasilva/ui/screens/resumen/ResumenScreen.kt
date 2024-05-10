package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginEvent

@Composable
fun ResumenScreen(){
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
    ) { _ ->
        /* LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(LoginEvent.MessageSeen)
            }
        }*/
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.big_size_space))
        ){
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
            DualSegmentPieChart( data = mapOf(
                Pair("Ingresos", 150.0),
                Pair("Gastos", 120.0),
                ))
        }
    }
}