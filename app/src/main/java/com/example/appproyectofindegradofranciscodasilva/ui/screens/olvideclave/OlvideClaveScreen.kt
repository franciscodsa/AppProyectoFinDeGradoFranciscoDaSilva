package com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun OlvideClaveScreen(viewModel: OlvideClaveViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->

        LaunchedEffect(state.message) {
            state.message?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(OlvideClaveEvent.MessageSeen)
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(dimensionResource(id = R.dimen.big_size_space)),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))

                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.cambia_clave),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Text(text = stringResource(R.string.primer_paso_cambio))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.handleEvent(OlvideClaveEvent.OnEmailChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.small_size_space)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.email)) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.handleEvent(OlvideClaveEvent.OnSendEmail) }) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "enviar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))

                Text(text = stringResource(R.string.segundo_paso_cambio))

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))

                OutlinedTextField(
                    value = state.authCode,
                    onValueChange = { viewModel.handleEvent(OlvideClaveEvent.OnAuthCodeChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.auth_code)) },
                    trailingIcon = {
                        IconButton(onClick = {
                            //Perimte pegar texto copiado usando el boton al final del outlined text field
                            val clipboardText = clipboardManager.getText()?.toString() ?: ""
                            viewModel.handleEvent(OlvideClaveEvent.OnAuthCodeChange(clipboardText))
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ContentPaste,
                                contentDescription = "pegar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))

                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = { viewModel.handleEvent(OlvideClaveEvent.OnNewPasswordChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.nueva_contrasena)) }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.handleEvent(
                            OlvideClaveEvent.OnConfirmPasswordChange(
                                it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.confirmar_contrasena)) }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight),
                    onClick = { viewModel.handleEvent(OlvideClaveEvent.OnChangePassword) }
                ) {
                    Text(text = "Cambiar contraseña")
                }
            }
        }
    }
}