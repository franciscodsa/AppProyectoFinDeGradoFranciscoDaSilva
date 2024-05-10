package com.example.appproyectofindegradofranciscodasilva.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.appproyectofindegradofranciscodasilva.R
import java.time.LocalDate

@Composable
fun RegisterScreen(
    innerPadding: PaddingValues
) {

    val scrollState = rememberScrollState()

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
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(dimensionResource(id = R.dimen.big_size_space)),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding())
            
            Box(
                modifier= Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(text = stringResource(R.string.crea_perfil))
            }

            //First name
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.nombre)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //last name
            OutlinedTextField(
                label = { Text(text = stringResource(R.string.apellidos)) },
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))


            //Phone field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.telefono)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //date of birth
            CustomDateField(
                value = LocalDate.of(2000, 1, 1),
                onValueChanged = {}
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))


            //email field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.email)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))


            OutlinedTextField(
                value = "",
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.contrasena)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.confirmar_contrasena)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, disabledContentColor = Color.White
                ),
            ) {
                Text(text = stringResource(R.string.registrarse))
            }
        }


    }


}


@Composable
fun CustomDateField(
    value: LocalDate,
    onValueChanged: (LocalDate) -> Unit
) {

    Row() {
        OutlinedTextField(
            label = { Text(stringResource(R.string.anyo)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = value.year.toString(),
            onValueChange = {
            }
        )


        OutlinedTextField(
            label = { Text(stringResource(R.string.mes)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = value.monthValue.toString(),
            onValueChange = {

            }
        )


        OutlinedTextField(
            label = { Text(stringResource(R.string.dia)) },
            modifier = Modifier.fillMaxWidth(1f),
            value = value.dayOfMonth.toString(),
            onValueChange = {

            }
        )
    }
}


/*
@Preview(showBackground = true)
@Composable
fun ComposablePreview() {
    RegisterScreen()
}*/
