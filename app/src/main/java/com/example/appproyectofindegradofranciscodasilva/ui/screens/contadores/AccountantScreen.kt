package com.example.appproyectofindegradofranciscodasilva.ui.screens.contadores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.data.model.Accountant
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.SwipeToDeleteContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountantScreen(
    viewModel: AccountantViewModel = hiltViewModel(),
    onAddClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(AccountantEvent.SetUserRole)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contadores") },
                actions = {
                    if (state.userRole == "admin") {
                        IconButton(onClick = onAddClick) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar Contador",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LaunchedEffect(state.message) {
            state.message?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(AccountantEvent.MessageSeen)
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.big_size_space))
                    .fillMaxSize()
            ) {
                LazyColumn {
                    items(state.accountants) { accountant ->
                        SwipeToDeleteContainer(
                            item = accountant,
                            onDelete = { viewModel.handleEvent(AccountantEvent.DeleteAccountant(it.email)) }
                        ) { accountant ->
                            AccountantCard(
                                accountant = accountant,
                                expanded = state.expandedAccountantId == accountant.email,
                                onExpandChange = {
                                    viewModel.handleEvent(
                                        AccountantEvent.OnAccountantExpandChanged(accountant.email)
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
}

@Composable
fun AccountantCard(
    accountant: Accountant,
    expanded: Boolean,
    onExpandChange: () -> Unit
) {
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
                Column {
                    Text(
                        text = "${accountant.firstName} ${accountant.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = accountant.email, style = MaterialTheme.typography.bodyMedium)
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
                Text(
                    text = "Teléfono: ${accountant.phone}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fecha de Nacimiento: ${accountant.dateOfBirth}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
