package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.data.model.FilesInfo
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.FilterButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    viewModel: FileViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Facturas", style = MaterialTheme.typography.headlineMedium) }
            )
        },
        bottomBar = bottomNavigationBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(FileEvent.MessageSeen)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            FilterButtons(viewModel = viewModel, selectedFilter = state.value.selectedFilter)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.value.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = state.value.files, key = { file -> file.id }) { file ->
                            ExpandableFileCard(
                                file = file,
                                total = state.value.total,
                                iva = state.value.iva,
                                onDownloadClick = {
                                    viewModel.handleEvent(
                                        FileEvent.DownloadFile(context, file.id)
                                    )
                                },
                                onUpdateClick = { fileId, total, iva ->
                                     viewModel.handleEvent(FileEvent.UpdateFile(fileId, total, iva))
                                },
                                onTotalChange = { viewModel.handleEvent(FileEvent.OnTotalChange(it)) },
                                onIvaChange = { viewModel.handleEvent(FileEvent.OnIvaChange(it)) },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(FileEvent.LoadAllFiles)
    }
}


@Composable
fun FilterButtons(viewModel: FileViewModel, selectedFilter: FileFilter) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        FilterButton(
            text = "Todos",
            selected = selectedFilter == FileFilter.Todos,
            onClick = {
                viewModel.handleEvent(FileEvent.OnFilterChanged(FileFilter.Todos))
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "Ingresos",
            selected = selectedFilter == FileFilter.Ingresos,
            onClick = {
                viewModel.handleEvent(FileEvent.OnFilterChanged(FileFilter.Ingresos))
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "Gastos",
            selected = selectedFilter == FileFilter.Gastos,
            onClick = {
                viewModel.handleEvent(FileEvent.OnFilterChanged(FileFilter.Gastos))
            }
        )
    }
}

@Composable
fun ExpandableFileCard(
    file: FilesInfo,
    total: String,
    iva: String,
    onDownloadClick: () -> Unit,
    onUpdateClick: (Long, String, String) -> Unit,
    onTotalChange: (String) -> Unit,
    onIvaChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.InsertDriveFile,
                            contentDescription = "Archivo"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = file.fileName, style = MaterialTheme.typography.titleMedium)
                    }

                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Descripción: ${file.description}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Fecha: ${file.date}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (isEditing) {
                            OutlinedTextField(
                                value = total,
                                onValueChange = { onTotalChange(it) },
                                label = { Text("Total") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = iva,
                                onValueChange = { onIvaChange(it) },
                                label = { Text("IVA") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        } else {
                            Text(
                                text = "Total: ${file.total}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "IVA: ${file.iva}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Column {
                    Spacer(modifier = Modifier.width(8.dp))

                    if (expanded) {


                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = {
                            onUpdateClick(
                                file.balanceId,
                                total,
                                iva
                            )
                            isEditing = false


                        }) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Guardar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    } else {
                        IconButton(onClick = onDownloadClick) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Descargar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

        }
    }
}
