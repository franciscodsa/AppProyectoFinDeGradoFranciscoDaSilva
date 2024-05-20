package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: ResumeViewModel = hiltViewModel()
) {

    var openBottomSheet by remember { mutableStateOf(false) }

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openBottomSheet = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
    ) { _ ->
        LaunchedEffect(state.value.message) {
            state.value.message?.let {
                snackbarHostState.showSnackbar(
                    message = state.value.message.toString(), duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(ResumenEvent.MessageSeen)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.big_size_space))
        ) {
            Row {
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(0.66f),
                    expanded = state.value.expandedTrimestre,
                    onExpandedChange = { viewModel.handleEvent(ResumenEvent.OnTrimesterMenuExpandedChanged) }
                ) {
                    TextField(
                        value = state.value.selectedTrimester,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Trimestre") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = state.value.expandedTrimestre
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = state.value.expandedTrimestre,
                        onDismissRequest = { viewModel.handleEvent(ResumenEvent.OnTrimesterMenuExpandedChanged) }
                    ) {
                        state.value.trimestres.forEach { trimestre ->
                            DropdownMenuItem(
                                text = { Text(trimestre) },
                                onClick = {
                                    viewModel.handleEvent(ResumenEvent.OnTrimesterSelected(trimestre))
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(0.33f),
                    expanded = state.value.expandedYear,
                    onExpandedChange = { viewModel.handleEvent(ResumenEvent.OnYearMenuExpandedChanged) }
                ) {
                    TextField(
                        value = state.value.selectedYear,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Año") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = state.value.expandedYear
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = state.value.expandedYear,
                        onDismissRequest = { viewModel.handleEvent(ResumenEvent.OnYearMenuExpandedChanged) }
                    ) {
                        state.value.years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    viewModel.handleEvent(ResumenEvent.OnYearSelected(year))
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
            DualSegmentPieChart(
                data = mapOf(
                    Pair("Ingresos", state.value.income),
                    Pair("Gastos", state.value.expenses),
                )
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

            ExpandableCard(
                irpfAmount = "${state.value.irpf}",
                ivaAmount = "${state.value.iva}",
                warningMessage = "Recuerda tener suficiente dinero en la cuenta bancaria al final de cada trimestre para evitar sanciones."
            )


            if (openBottomSheet) {
                BottomSheetContent(
                    onClose = { openBottomSheet = false },
                    onFileSelected = {viewModel.handleEvent(ResumenEvent.OnFileSelected(it))},
                    onMimeTypeSelected = {viewModel.handleEvent(ResumenEvent.OnMimeTypeSelected(it))},
                    onSubmit = {viewModel.handleEvent(ResumenEvent.UploadFile)}
                )
            }
        }
    }
}

@Composable
fun ExpandableCard(
    irpfAmount: String,
    ivaAmount: String,
    warningMessage: String
) {
    var expanded by remember { mutableStateOf(false) }
    val irpf = irpfAmount.toDoubleOrNull() ?: 0.0
    val iva = ivaAmount.toDoubleOrNull() ?: 0.0
    val total = irpf + iva

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Impuestos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: ${String.format("%.2f€", total)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "IRPF: ${String.format("%.2f€", irpf)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "IVA: ${String.format("%.2f€", iva)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = warningMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    onClose: () -> Unit,
    onMimeTypeSelected: (String) -> Unit,
    onFileSelected: (File) -> Unit,
    onSubmit: () -> Unit
) {

    var selectedFileName by remember { mutableStateOf("") }
    var field1 by remember { mutableStateOf("") }
    var field2 by remember { mutableStateOf("") }
    var field3 by remember { mutableStateOf("") }
    var checkbox1 by remember { mutableStateOf(false) }
    var checkbox2 by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onClose() },
        sheetState = bottomSheetState
    ) {

        val context = LocalContext.current
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
                result?.let { uri ->
                    val contentResolver = context.contentResolver

                    //Conseguir el tipo de archivo
                    val mimeType = contentResolver.getType(uri) ?: ""

                    //Conseguir el nombre del archivo
                    val cursor = context.contentResolver.query(uri, null, null, null, null)
                    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor?.moveToFirst()
                    val name = nameIndex?.let { cursor.getString(it) }
                    cursor?.close()

                    //Crear el File con lo conseguido del selector
                    val selectedFile =
                        File(context.cacheDir, name ?: "") // You can use any desired file name here


                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        selectedFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    selectedFileName = name?:""

                    onMimeTypeSelected(mimeType)
                    onFileSelected(selectedFile)
                }
            }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Añadir factura",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = { onClose() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }


            var ingresosSelected by remember { mutableStateOf(true) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                IngresosGastosToggle(
                    onIngresosSelected = { ingresosSelected = true },
                    onGastosSelected = { ingresosSelected = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = field1,
                onValueChange = { field1 = it },
                label = { Text("Field 1") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = field2,
                onValueChange = { field2 = it },
                label = { Text("Field 2") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = field3,
                onValueChange = { field3 = it },
                label = { Text("Field 3") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = selectedFileName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Archivo seleccionado")}
                )
            }

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { launcher.launch("*/*") }) {
                    Text("Seleccionar archivo")
                }
                Button(
                    onClick = {
                        onSubmit()
                    },
                ) {
                    Text("Subir")
                }
            }
            Spacer(modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight))
        }
    }
}

@Composable
fun IngresosGastosToggle(
    onIngresosSelected: () -> Unit,
    onGastosSelected: () -> Unit
) {
    var ingresosSelected by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleButton(
            text = "Ingresos",
            selected = ingresosSelected,
            onClick = {
                ingresosSelected = true
                onIngresosSelected()
            }
        )
        ToggleButton(
            text = "Gastos",
            selected = !ingresosSelected,
            onClick = {
                ingresosSelected = false
                onGastosSelected()
            }
        )
    }
}

@Composable
fun ToggleButton(
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
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}