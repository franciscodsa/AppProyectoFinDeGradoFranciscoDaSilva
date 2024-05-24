package com.example.appproyectofindegradofranciscodasilva.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    toRegistroScreen: () -> Unit,
    toClaveOlvidadaScreen: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        bottomBar = bottomNavigationBar
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.big_size_space)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AppLogo()
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
                    LoginForm(
                        state.value.email,
                        state.value.password,
                        { viewModel.handleEvent(LoginEvent.OnEmailTextChange(it)) },
                        { viewModel.handleEvent(LoginEvent.OnPasswordTextChange(it)) },
                        { viewModel.handleEvent(LoginEvent.Login) },
                        toRegistroScreen,
                        toClaveOlvidadaScreen
                    )
                }
            }

            LaunchedEffect(state.value.message) {
                state.value.message?.let {
                    snackbarHostState.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short
                    )
                    viewModel.handleEvent(LoginEvent.MessageSeen)
                }
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
fun AppLogo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ContaEasy",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
        Image(
            painter = painterResource(id = R.drawable.ic_contaeasy),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bigger_size_space)))
    }
}

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsernameField(email, onEmailChange)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))
        PasswordField(password, onPasswordChange)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
        LoginButton(onLoginClick)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
        TextLink(text = "Registrarse", onClick = onRegisterClick)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
        TextLink(text = "¿Has olvidado tu contraseña?", onClick = onForgotPasswordClick)
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(R.string.login))
    }
}

@Composable
fun PasswordField(
    password: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onTextChanged,
        label = { Text(text = stringResource(R.string.contrasena)) },
        placeholder = { Text(text = stringResource(R.string.contrasena)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun UsernameField(
    email: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextChanged,
        label = { Text(text = stringResource(R.string.email)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.clickable { onClick() }
    )
}
