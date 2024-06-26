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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.data.model.Client
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.FilterButton
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.SwipeToDeleteContainer
import com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen.CustomDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientScreen(
    viewModel: ClientViewModel = hiltViewModel(),
    onFilesClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(ClientEvent.SetUserRole)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes", style = MaterialTheme.typography.headlineMedium) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LaunchedEffect(state.message) {
            state.message?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(ClientEvent.MessageSeen)
            }
        }


        if (state.userRole == "admin") {
            LaunchedEffect(Unit) {
                viewModel.handleEvent(ClientEvent.LoadClients)
            }
        } else {
            LaunchedEffect(Unit) {
                viewModel.handleEvent(ClientEvent.LoadClientsByAccountant)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.handleEvent(ClientEvent.GetAccountantsEmails)
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.big_size_space))
                .fillMaxSize()
        ) {
            if (state.userRole == "admin") {
                FilterButtons(
                    selectedFilter = state.selectedFilter,
                    onFilterChange = { viewModel.handleEvent(ClientEvent.OnFilterChanged(it)) }
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_size_space)))
            LazyColumn {
                items(state.clients, key = { client -> client.email }) { client ->
                    SwipeToDeleteContainer(
                        item = client,
                        onDelete = { viewModel.handleEvent(ClientEvent.DeleteClient(it.email)) }
                    ) { client ->
                        ClientCard(
                            client = client,
                            expanded = state.expandedClientId == client.email,
                            accountantEmails = state.accountantEmails,
                            selectedAccountantEmail = state.selectedAccountantEmail,
                            onExpandChange = {
                                viewModel.handleEvent(
                                    ClientEvent.OnClientExpandChanged(client.email)
                                )
                            },
                            onFilesClick = onFilesClick,
                            onAccountantEmailChange = {
                                viewModel.handleEvent(
                                    ClientEvent.OnAccountantEmailChanged(it)
                                )
                            },
                            onSaveNewClientsAccountant = {
                                viewModel.handleEvent(
                                    ClientEvent.OnSaveNewClientsAccountant(client)
                                )
                            },
                            onAccountantEmailSelected = {
                                viewModel.handleEvent(
                                    ClientEvent.OnAccountantEmailSelected(it)
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
                }
            }
        }
    }


}


@Composable
fun FilterButtons(
    selectedFilter: ClientFilter,
    onFilterChange: (ClientFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        FilterButton(
            text = "Todos",
            selected = selectedFilter == ClientFilter.Todos,
            onClick = {
                onFilterChange(ClientFilter.Todos)
            }
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_size_space)))
        FilterButton(
            text = "No Asignados",
            selected = selectedFilter == ClientFilter.NoAsignados,
            onClick = {
                onFilterChange(ClientFilter.NoAsignados)
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
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.medium_size_space)),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.big_size_space))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Client")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_size_space)))
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
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
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))

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
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_size_space)))
                        Row {
                            Button(onClick = { onFilesClick(client.email) }) {
                                Text(text = "Archivos")
                            }

                        }
                    }
                    Column {
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (isEditing) {
                            IconButton(onClick = {
                                onAccountantEmailChange(selectedAccountantEmail)
                                onSaveNewClientsAccountant()
                                isEditing = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Save",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}