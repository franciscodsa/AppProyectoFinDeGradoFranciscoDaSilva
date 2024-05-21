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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import java.time.LocalDate
import java.time.Month

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }



    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(RegisterEvent.MessageSeen)
            }
        }


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
                value = state.value.firstName,
                onValueChange = {viewModel.handleEvent(RegisterEvent.OnFirstNameTextChange(it))},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.nombre)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //last name
            OutlinedTextField(
                value = state.value.lastNames,
                onValueChange = {viewModel.handleEvent(RegisterEvent.OnLastNameChange(it))},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.apellidos)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))


            //Phone field
            OutlinedTextField(
                value = state.value.phone,
                onValueChange = {viewModel.handleEvent(RegisterEvent.OnPhoneTextChange(it))},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.telefono)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //date of birth
            CustomDateField(
                year = state.value.year,
                month= state.value.month,
                day= state.value.day,
                onYearFieldChange= {viewModel.handleEvent(RegisterEvent.OnYearFieldChange(it))},
                onMonthFieldChange = {viewModel.handleEvent(RegisterEvent.OnMonthFieldChange(it))},
                onDayFieldChange = {viewModel.handleEvent(RegisterEvent.OnDayFieldChange(it))}
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))


            //email field
            OutlinedTextField(
                value = state.value.email,
                onValueChange = {viewModel.handleEvent(RegisterEvent.OnEmailTextChange(it))},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.email)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))


            //Password
            OutlinedTextField(
                value = state.value.password,
                onValueChange = { viewModel.handleEvent(RegisterEvent.OnPasswordTextChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.contrasena)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //Password confirmation
            OutlinedTextField(
                value = state.value.confirmPassword,
                onValueChange = {viewModel.handleEvent(RegisterEvent.OnPasswordConfirmTextChange(it))},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.confirmar_contrasena)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

            Button(
                onClick = {viewModel.handleEvent(RegisterEvent.Register)},
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
    year: String,
    month: String,
    day: String,
    onYearFieldChange: (String) -> Unit,
    onMonthFieldChange: (String) -> Unit,
    onDayFieldChange: (String) -> Unit
) {

    Row() {
        OutlinedTextField(
            label = { Text(stringResource(R.string.anyo)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = year,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {onYearFieldChange(it.take(4))}
        )


        OutlinedTextField(
            label = { Text(stringResource(R.string.mes)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = month,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {onMonthFieldChange(it.take(2))            }
        )


        OutlinedTextField(
            label = { Text(stringResource(R.string.dia)) },
            modifier = Modifier.fillMaxWidth(1f),
            value = day,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {onDayFieldChange(it.take(2))}
        )
    }
}
