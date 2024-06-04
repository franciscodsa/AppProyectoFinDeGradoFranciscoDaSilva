package com.example.appproyectofindegradofranciscodasilva.ui.screens.profile

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogOutClick: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  Text(text = stringResource(R.string.mi_perfil), style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = {
                        viewModel.handleEvent(ProfileEvent.Logout)
                        onLogOutClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = stringResource(R.string.logout),
                            tint = Color.Red
                        )
                    }
                }
            )
        },
        bottomBar = bottomNavigationBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = it, duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(ProfileEvent.MessageSeen)
            }
        }


        LaunchedEffect(Unit){
            viewModel.handleEvent(ProfileEvent.LoadUserData)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding())

            //First name
            OutlinedTextField(
                value = state.value.firstName,
                onValueChange = { viewModel.handleEvent(ProfileEvent.OnFirstNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.nombre)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //Last name
            OutlinedTextField(
                value = state.value.lastName,
                onValueChange = { viewModel.handleEvent(ProfileEvent.OnLastNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.apellidos)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //Phone field
            OutlinedTextField(
                value = state.value.phone,
                onValueChange = { viewModel.handleEvent(ProfileEvent.OnPhoneChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.telefono)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

            //Date of birth
            CustomDateField(
                year = state.value.year,
                month = state.value.month,
                day = state.value.day,
                onYearFieldChange = { viewModel.handleEvent(ProfileEvent.OnYearFieldChange(it)) },
                onMonthFieldChange = { viewModel.handleEvent(ProfileEvent.OnMonthFieldChange(it)) },
                onDayFieldChange = { viewModel.handleEvent(ProfileEvent.OnDayFieldChange(it)) }
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

            Button(
                onClick = { viewModel.handleEvent(ProfileEvent.SaveChanges) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, disabledContentColor = Color.White
                ),
            ) {
                Text(text = stringResource(R.string.guardar_cambios))
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
    Row {
        OutlinedTextField(
            label = { Text(stringResource(R.string.anyo)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = year,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { onYearFieldChange(it.take(4)) }
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.mes)) },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = month,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { onMonthFieldChange(it.take(2)) }
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.dia)) },
            modifier = Modifier.fillMaxWidth(1f),
            value = day,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { onDayFieldChange(it.take(2)) }
        )
    }
}
