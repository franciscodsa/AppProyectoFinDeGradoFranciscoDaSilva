package com.example.appproyectofindegradofranciscodasilva.ui.screens.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.data.model.Client


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel(),
    onChatClick: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.handleEvent(ContactsEvent.SetUserRole)
        viewModel.handleEvent(ContactsEvent.LoadCurrentUser)
        viewModel.handleEvent(ContactsEvent.LoadContacts)
    }


    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Contactos") }
                )
            },
            bottomBar = bottomNavigationBar
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                if (state.userRole == "user") {
                    AccountantCard(
                        accountant = state.accountant,
                        currentUser = state.currentUser,
                        onChatClick = onChatClick
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(state.clients) { client ->
                            ClientCard(
                                client = client,
                                onChatClick = onChatClick

                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ClientCard(
    client: Client,
    onChatClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${client.firstName} ${client.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = client.email, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onChatClick(client.email) }) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun AccountantCard(
    accountant: Accountant,
    currentUser: String,
    onChatClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${accountant.firstName} ${accountant.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = accountant.email, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onChatClick(currentUser) }) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}