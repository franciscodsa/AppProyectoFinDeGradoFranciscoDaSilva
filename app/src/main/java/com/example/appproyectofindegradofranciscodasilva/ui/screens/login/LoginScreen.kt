package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()


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
                viewModel.handleEvent(LoginEvent.MessageSeen)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.big_size_space))
        ) {
            AppLogo()
            Login(
                Modifier.align(Alignment.Center),
                state.value.username,
                state.value.password,
                state.value.isLoading,
                { viewModel.handleEvent(LoginEvent.onPasswordTextChange(it)) },
                { viewModel.handleEvent(LoginEvent.onUsernameTextChange(it)) },
                { viewModel.handleEvent(LoginEvent.Login) },
            )
        }


    }
}

@Composable
fun Login(
    modifier: Modifier,
    username: String,
    password: String,
    isLoading: Boolean,
    onChangePasswordText: (String) -> Unit,
    onChangerUsernameText: (String) -> Unit,
    onLoginSelected: () -> Unit,
) {
    if (isLoading) {
        Box(
            Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {

        Column(modifier = modifier) {


            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))
            UsernameField(username) {
                onChangerUsernameText(it)
            }
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
            PasswordField(password) {
                onChangePasswordText(it)
            }


            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
            LoginButton {
                //onLoginSelected()
            }
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))


        }


    }

}

@Composable
fun AppLogo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

        Text(text = "Welcome")

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
        //App logo
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Logo"
            )
        }
    }


}

@Composable
fun LoginButton(onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, disabledContentColor = Color.White
        ),
    ) {
        Text(text = stringResource(R.string.login))
    }
}


@Composable
fun PasswordField(
    password: String,
    onTextFieldChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onTextFieldChanged,
        placeholder = { Text(text = stringResource(R.string.contrasena)) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        label = { Text(text = stringResource(R.string.contrasena)) }
    )
}

@Composable
fun UsernameField(
    email: String, onTextFieldChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(R.string.email)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
        label = { Text(text = stringResource(R.string.email)) }
    )
}

/*
@Preview(showBackground = true)
@Composable
fun composablePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        AppLogo()
        Login(
            Modifier.align(Alignment.Center),
            "aaaa",
            "aaaa",
            false
        )
    }
}*/
