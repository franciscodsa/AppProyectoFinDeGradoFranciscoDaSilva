package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),

    onLogin: () -> Unit,
    toRegistroScreen: () -> Unit,
    toClaveOlvidadaScreen: () -> Unit,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()


    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
    ) { _ ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(LoginEvent.MessageSeen)
            }
        }

        if (state.value.isLoading) {
            Box(
                Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AppLogo()
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))
                Login(
                    Modifier
                        .align(Alignment.Center)
                        .padding(dimensionResource(id = R.dimen.big_size_space)),
                    state.value.email,
                    state.value.password,
                    { viewModel.handleEvent(LoginEvent.OnPasswordTextChange(it)) },
                    { viewModel.handleEvent(LoginEvent.OnEmailTextChange(it)) },
                    { viewModel.handleEvent(LoginEvent.Login) },
                    { toRegistroScreen() },
                    { toClaveOlvidadaScreen() }
                )
            }


            LaunchedEffect(state.value.logged) {
                if (state.value.logged) {
                    onLogin()
                }
            }
        }


    }
}

@Composable
fun Login(
    modifier: Modifier,
    username: String,
    password: String,
    onChangePasswordText: (String) -> Unit,
    onChangerUsernameText: (String) -> Unit,
    onLoginSelected: () -> Unit,
    toRegistroScreen: () -> Unit,
    toClaveOlvidadaScreen: () -> Unit
) {


    Column(modifier = modifier) {

        UsernameField(username) {
            onChangerUsernameText(it)
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
        PasswordField(password) {
            onChangePasswordText(it)
        }

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
        LoginButton {
            onLoginSelected()
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Cyan)
                .clickable { toRegistroScreen() }
        ) {
            Text(
                text = "Registrarse",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Red)
                .clickable { toClaveOlvidadaScreen() }
        ) {
            Text(
                text = "¿Has olividado tu contraseña?",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
    }


}

@Composable
fun AppLogo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
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
