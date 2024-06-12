package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appproyectofindegradofranciscodasilva.R
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    clientId: String,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load messages when entering the screen
    LaunchedEffect(Unit) {
        viewModel.handleEvent(ChatEvent.OnClientEmailChange(clientId))
        viewModel.handleEvent(ChatEvent.LoadMessages)
        viewModel.handleEvent(ChatEvent.LoadCurrentUser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chat") }
            )
        }
    ) { innerPadding ->

        LaunchedEffect(state.errorMessage) {
            state.errorMessage?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(ChatEvent.MessageSeen)
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.medium_size_space)),
                    reverseLayout = true
                ) {
                    items(state.messages) { message ->
                        val isCurrentUser =
                            message.senderEmail == state.currentUser
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                        ) {
                            if (isCurrentUser) Spacer(modifier = Modifier.weight(0.2f))
                            Card(
                                modifier = Modifier
                                    .padding(dimensionResource(id = R.dimen.small_size_space))
                                    .weight(0.8f),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(id = R.dimen.medium_size_space))
                                ) {
                                    Text(
                                        text = message.senderEmail,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))
                                    Text(
                                        text = message.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_size_space)))
                                    Text(
                                        text = SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss",
                                            Locale.getDefault()
                                        ).format(message.timestamp),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                            if (!isCurrentUser) Spacer(modifier = Modifier.weight(0.2f))
                        }
                    }
                }

                TextField(
                    value = state.message,
                    placeholder = { Text(text = "Escribe aquí") },
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.handleEvent(ChatEvent.SendMessage) },
                            enabled = !state.isLoading,
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onValueChange = { viewModel.handleEvent(ChatEvent.OnMessageTextChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.medium_size_space))
                )

            }
        }
    }
}