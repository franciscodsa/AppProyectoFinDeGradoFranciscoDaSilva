package com.example.appproyectofindegradofranciscodasilva.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.FilterButton
import com.example.appproyectofindegradofranciscodasilva.ui.screens.clients.ClientFilter

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(Unit) {
        viewModel.handleEvent(RegisterEvent.SetUserRole)
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

            if (state.value.userRole == "admin"){
                UserTypeButtons(
                    selectedUserType = state.value.selectedUserType,
                    onSelectedUserType = {viewModel.handleEvent(RegisterEvent.OnSelectedUserType(it))}
                )
                Box(
                    modifier= Modifier
                        .weight(0.1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = if (state.value.selectedUserType == UserType.Cliente) "Crea un cliente" else "Crea un contador", style = MaterialTheme.typography.headlineMedium)
                }

            }else{
                Box(
                    modifier= Modifier
                        .weight(0.1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){

                    Text(text = stringResource(R.string.crea_perfil), style = MaterialTheme.typography.headlineMedium)
                }
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

            //Last name
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


            //Email field
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
            if (state.value.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
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


}

@Composable
fun UserTypeButtons(
    selectedUserType: UserType,
    onSelectedUserType: (UserType) -> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ){
        Spacer(modifier = Modifier.weight(1f))
        FilterButton(
            text = "Cliente",
            selected = selectedUserType == UserType.Cliente,
            onClick = {
                onSelectedUserType(UserType.Cliente)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "Contador",
            selected = selectedUserType == UserType.Contador,
            onClick = {
                onSelectedUserType(UserType.Contador)
            }
        )
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
