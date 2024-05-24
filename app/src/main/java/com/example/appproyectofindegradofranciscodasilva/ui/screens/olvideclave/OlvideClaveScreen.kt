package com.example.appproyectofindegradofranciscodasilva.ui.screens.olvideclave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun OlvideClaveScreen() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        /*LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(LoginEvent.MessageSeen)
            }
        }*/


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.big_size_space)),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding())

            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.cambia_clave), style = MaterialTheme.typography.headlineMedium)
            }

            Column(modifier = Modifier.weight(0.7f)) {

                Text(text = stringResource(R.string.primer_paso_cambio))

                Row(Modifier.height(OutlinedTextFieldDefaults.MinHeight)) {
                    //Email
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier.weight(0.80F),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        maxLines = 1,
                        label = { Text(text = stringResource(R.string.email)) }
                    )

                    Button(
                        modifier = Modifier
                            .weight(0.20F)
                            .fillMaxHeight()
                            .padding(
                                top = dimensionResource(id = R.dimen.medium_size_space)
                            ),
                        shape = OutlinedTextFieldDefaults.shape,
                        onClick = { /*TODO*/ }) {



                        Icon(imageVector = androidx.compose.material.icons.Icons.Filled.Send, contentDescription = "enviar")

                    }
                }

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

                Text(text = stringResource(R.string.segundo_paso_cambio))

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.nueva_contrasena)) }
                )

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.confirmar_contrasena)) }
                )

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight), onClick = { /*TODO*/ }) {
                    Text(text = "Cambiar contraseña")

                }

            }



        }


    }
}
