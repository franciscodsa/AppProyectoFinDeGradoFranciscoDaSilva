package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

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
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.data.model.FilesInfo
import com.example.appproyectofindegradofranciscodasilva.ui.screens.register.RegisterEvent
import com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen.ToggleButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(viewModel: FileViewModel = hiltViewModel()) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(FileEvent.MessageSeen)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            FilterButtons(viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = state.value.files, key = { file -> file.id }) { file ->
                    ExpandableFileCard(
                        file = file,
                        onDownloadClick = { viewModel.handleEvent(FileEvent.DownloadFile(context, file.id)) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
    LaunchedEffect(Unit){
        viewModel.handleEvent(FileEvent.LoadAllFiles)
    }
}

@Composable
fun FilterButtons(viewModel: FileViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        ToggleButton(
            text = "Todos",
            selected = true,
            onClick = { viewModel.handleEvent(FileEvent.LoadAllFiles) }
        )
        ToggleButton(
            text = "Ingresos",
            selected = false,
            onClick = { viewModel.handleEvent(FileEvent.LoadIncomeFiles) }
        )
        ToggleButton(
            text = "Gastos",
            selected = false,
            onClick = { viewModel.handleEvent(FileEvent.LoadExpenseFiles) }
        )
    }
}

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.InsertDriveFile, contentDescription = "Archivo")
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
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDownloadClick) {
                    Text(text = "Descargar")
                }
            }
        }
    }
}


//@Composable
//fun FileSelectionScreen(
//    viewModel: FileViewModel = hiltViewModel()
//) {
//    val state = viewModel.uiState.collectAsStateWithLifecycle()
//
//    val context = LocalContext.current
//    val launcher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
//            result?.let { uri ->
//                val contentResolver = context.contentResolver
//
//                //Conseguir el tipo de archivo
//                val mimeType = contentResolver.getType(uri)?:""
//
//                //Conseguir el nombre del archivo
//                val cursor = context.contentResolver.query(uri, null, null, null, null)
//                val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                cursor?.moveToFirst()
//                val name = nameIndex?.let { cursor.getString(it) }
//                cursor?.close()
//
//                //Crear el File con lo conseguido del selector
//                val selectedFile = File(context.cacheDir, name?:"") // You can use any desired file name here
//
//
//                contentResolver.openInputStream(uri)?.use { inputStream ->
//                    selectedFile.outputStream().use { outputStream ->
//                        inputStream.copyTo(outputStream)
//                    }
//                }
//
//                viewModel.handleEvent(FileEvent.OnMimeTypeSelected(mimeType))
//                viewModel.handleEvent(FileEvent.OnFileSelected(selectedFile))
//            }
//        }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = { launcher.launch("*/*") }) {
//            Text("Seleccionar archivo")
//        }
//        Button(onClick = { viewModel.handleEvent(FileEvent.UploadFile) }) {
//            Text(text = "Subir")
//        }
//
//        OutlinedTextField(
//            value = state.value.fileId,
//            onValueChange = {
//                viewModel.handleEvent(FileEvent.OnFileIdChange(it))
//            },
//            label = {
//                Text(text = "Enter id")
//            },
//            maxLines = 1,
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Button(onClick = { viewModel.handleEvent(FileEvent.DownloadFile(context)) }) {
//            Text(text = "descargar")
//        }
//    }
//}