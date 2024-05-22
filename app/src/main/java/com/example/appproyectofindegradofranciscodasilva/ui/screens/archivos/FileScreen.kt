package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            FilterButtons(viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.value.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = state.value.files, key = { file -> file.id }) { file ->
                            ExpandableFileCard(
                                file = file,
                                onDownloadClick = {
                                    viewModel.handleEvent(
                                        FileEvent.DownloadFile(context, file.id)
                                    )
                                }
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
fun FilterButtons(viewModel: FileViewModel) {
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
                viewModel.handleEvent(FileEvent.LoadAllFiles)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "Ingresos",
            selected = selectedFilter == "Ingresos",
            onClick = {
                selectedFilter = "Ingresos"
                viewModel.handleEvent(FileEvent.LoadIncomeFiles)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterButton(
            text = "Gastos",
            selected = selectedFilter == "Gastos",
            onClick = {
                selectedFilter = "Gastos"
                viewModel.handleEvent(FileEvent.LoadExpenseFiles)
            }
        )
    }
}

/*@Composable
fun SmallToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.3f
            ),
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier
            .height(ButtonDefaults.MinHeight)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp
        )
    }
}*/

@Composable
fun ExpandableFileCard(file: FilesInfo, onDownloadClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
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
                        Text(text = "Fecha: ${file.date}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDownloadClick) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Descargar", tint = MaterialTheme.colorScheme.primary)
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
