package com.example.appproyectofindegradofranciscodasilva.ui.screens.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.FilterButton
import com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen.CustomDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientScreen(
    viewModel: ClientViewModel = hiltViewModel(),
    onChatClick: (String) -> Unit,
    onFilesClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                FilterButtons(
                    onLoadClients = { viewModel.handleEvent(ClientEvent.LoadClients) }
                ) { viewModel.handleEvent(ClientEvent.LoadClientsWithNoAccountant) }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(state.clients) { client ->
                        ClientCard(
                            client = client,
                            expanded = state.expandedClientId == client.email,
                            accountantEmails = state.accountantEmails,
                            selectedAccountantEmail = state.selectedAccountantEmail ?: "",
                            onExpandChange = {
                                viewModel.handleEvent(
                                    ClientEvent.OnClientExpandChanged(
                                        client.email
                                    )
                                )
                            },
                            onChatClick = onChatClick,
                            onFilesClick = onFilesClick,
                            onAccountantEmailChange = {
                                viewModel.handleEvent(
                                    ClientEvent.OnAccountantEmailChanged(
                                        it
                                    )
                                )
                            },
                            onSaveNewClientsAccountant = {
                                viewModel.handleEvent(
                                    ClientEvent.OnSaveNewClientsAccountant(
                                        client
                                    )
                                )
                            },
                            onAccountantEmailSelected = {
                                viewModel.handleEvent(
                                    ClientEvent.OnAccountantEmailSelected(
                                        it
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun FilterButtons(
    onLoadClients: () -> Unit,
    onLoadClientsWithNoAccountant: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        FilterButton(
            text = "Todos",
            selected = selectedFilter == "Todos",
            onClick = {
                selectedFilter = "Todos"
                onLoadClients()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "No Asignados",
            selected = selectedFilter == "No Asignado",
            onClick = {
                selectedFilter = "No Asignado"
                onLoadClientsWithNoAccountant()
            }
        )
    }
}

@Composable
fun ClientCard(
    client: Client,
    expanded: Boolean,
    accountantEmails: List<String>,
    selectedAccountantEmail: String,
    onExpandChange: () -> Unit,
    onChatClick: (String) -> Unit,
    onFilesClick: (String) -> Unit,
    onAccountantEmailChange: (String) -> Unit,
    onSaveNewClientsAccountant: () -> Unit,
    onAccountantEmailSelected: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var expandedDropdown by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandChange() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Client")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${client.firstName} ${client.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = client.email, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onExpandChange() }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand"
                    )
                }

            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Teléfono: ${client.phone}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Fecha de Nacimiento: ${client.dateOfBirth}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isEditing) {
                                CustomDropdown(
                                    modifier = Modifier.weight(1f),
                                    label = "Contador",
                                    selectedText = selectedAccountantEmail,
                                    expanded = expandedDropdown,
                                    onExpandedChange = { expandedDropdown = !expandedDropdown },
                                    items = accountantEmails,
                                    onItemSelected = { onAccountantEmailSelected(it) }
                                )
                            } else {
                                Text(
                                    text = "Contador: ${client.accountantEmail}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { onChatClick(client.email) }) {
                                Text("Chat")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { onFilesClick(client.email) }) {
                                Text("Archivos")
                            }
                        }
                    }
                    Column {
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        if (isEditing) {
                            IconButton(onClick = {
                                onAccountantEmailChange(selectedAccountantEmail)
                                onSaveNewClientsAccountant()
                                isEditing = false
                            }) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                            }
                        }


                    }
                }

            }
        }
    }
}

/*
@Composable
fun ClientCard(
    client: Client,
    expanded: Boolean,
    onExpandChange: () -> Unit,
    onChatClick: (String) -> Unit,
    onFilesClick: (String) -> Unit,
    onAccountantEmailChange: (String) -> Unit,
    onSaveAccountantEmail: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var accountantEmail by remember { mutableStateOf(client.accountantEmail) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandChange() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Client")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${client.firstName} ${client.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = client.email, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onExpandChange() }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand"
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Teléfono: ${client.phone}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fecha de Nacimiento: ${client.dateOfBirth}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isEditing) {
                        CustomDropdown(
                            modifier = Modifier.weight(1f),
                            label = "Contador",
                            selectedText = accountantEmail,
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = !expandedDropdown },
                            items = listOf("accountant1@example.com", "accountant2@example.com"),
                            onItemSelected = { accountantEmail = it }
                        )
                        IconButton(onClick = {
                            onAccountantEmailChange(accountantEmail)
                            onSaveAccountantEmail()
                            isEditing = false
                        }) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "Save")
                        }
                    } else {
                        Text(
                            text = "Contador: ${client.accountantEmail}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = { isEditing = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = { onChatClick(client.email) }) {
                        Text("Chat")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onFilesClick(client.email) }) {
                        Text("Archivos")
                    }
                }
            }
        }
    }
}
*/
