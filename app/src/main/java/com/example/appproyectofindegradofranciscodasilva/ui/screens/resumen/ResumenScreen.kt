package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.ui.navigation.FilterButton
import java.io.File

@Composable
fun ResumenScreen(
    viewModel: ResumeViewModel = hiltViewModel(),
    onClientsClick: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAccountansClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    var openBottomSheet by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(ResumenEvent.SetUserRole)
    }



    Scaffold(
        floatingActionButton = {
            if (state.userRole == "user") {
                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
                }
            } else {
                ExpandableFloatingActionButton(
                    expanded = isExpanded,
                    onExpandChange = { isExpanded = it },
                    onClientesClick = { onClientsClick() },
                    onContadoresClick = { onAccountansClick() }
                )
            }
        },
        bottomBar = bottomNavigationBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        LaunchedEffect(state.message) {
            state.message?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.handleEvent(ResumenEvent.MessageSeen)
            }
        }

        if (state.userRole == "user") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.big_size_space))
            ) {
                TopSection(state, viewModel)
                ContentSection(state)
                if (openBottomSheet) {
                    BottomSheetContent(
                        state,
                        onClose = { openBottomSheet = false },
                        onFileSelected = { viewModel.handleEvent(ResumenEvent.OnFileSelected(it)) },
                        onMimeTypeSelected = {
                            viewModel.handleEvent(
                                ResumenEvent.OnMimeTypeSelected(
                                    it
                                )
                            )
                        },
                        onInvoiceTypeChange = { viewModel.handleEvent(ResumenEvent.OnInvoiceTypeSelected) },
                        onTotalChange = { viewModel.handleEvent(ResumenEvent.OnNewInvoiceTotal(it)) },
                        onIvaChange = { viewModel.handleEvent(ResumenEvent.OnNewInvoiceIva(it)) },
                        onDescriptionChange = {
                            viewModel.handleEvent(
                                ResumenEvent.OnNewInvoiceDescription(
                                    it
                                )
                            )
                        },
                        onSubmit = { viewModel.handleEvent(ResumenEvent.UploadFile) }
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_contaeasy),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bigger_size_space)))

                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }


    }
}

@Composable
fun TopSection(state: ResumenState, viewModel: ResumeViewModel) {
    Row {
        CustomDropdown(
            modifier = Modifier.weight(0.66f),
            label = "Trimestre",
            selectedText = state.selectedTrimester,
            expanded = state.expandedTrimestre,
            onExpandedChange = { viewModel.handleEvent(ResumenEvent.OnTrimesterMenuExpandedChanged) },
            items = state.trimestres,
            onItemSelected = { viewModel.handleEvent(ResumenEvent.OnTrimesterSelected(it)) }
        )

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))

        CustomDropdown(
            modifier = Modifier.weight(0.33f),
            label = "Año",
            selectedText = state.selectedYear,
            expanded = state.expandedYear,
            onExpandedChange = { viewModel.handleEvent(ResumenEvent.OnYearMenuExpandedChanged) },
            items = state.years,
            onItemSelected = { viewModel.handleEvent(ResumenEvent.OnYearSelected(it)) }
        )
    }
}

@Composable
fun CustomDropdown(
    modifier: Modifier,
    label: String,
    selectedText: String,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            .padding(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange() }
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onExpandedChange()
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}


@Composable
fun ContentSection(state: ResumenState) {
    DualSegmentPieChart(
        data = mapOf(
            "Ingresos" to state.income,
            "Gastos" to state.expenses
        )
    )
    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
    ExpandableCard(
        irpfAmount = state.irpf.toString(),
        ivaAmount = state.iva.toString(),
        warningMessage = "Recuerda tener suficiente dinero en la cuenta bancaria al final de cada trimestre para evitar sanciones."
    )
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
            .shadow(4.dp, RoundedCornerShape(8.dp)),
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
    state: ResumenState,
    onClose: () -> Unit,
    onMimeTypeSelected: (String) -> Unit,
    onFileSelected: (File) -> Unit,
    onInvoiceTypeChange: () -> Unit,
    onTotalChange: (String) -> Unit,
    onIvaChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var selectedFileName by remember { mutableStateOf("") }
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
                    val mimeType = contentResolver.getType(uri) ?: ""
                    val cursor = context.contentResolver.query(uri, null, null, null, null)
                    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor?.moveToFirst()
                    val name = nameIndex?.let { cursor.getString(it) }
                    cursor?.close()
                    val selectedFile = File(context.cacheDir, name ?: "")
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        selectedFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    selectedFileName = name ?: ""
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IngresosGastosToggle(
                    isExpense = state.isExpense,
                    onInvoiceTypeChange = onInvoiceTypeChange
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.newInvoiceTotal,
                onValueChange = onTotalChange,
                label = { Text("Total") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.newInvoiceIva,
                onValueChange = onIvaChange,
                label = { Text("IVA") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.newInvoiceDescription,
                onValueChange = onDescriptionChange,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = selectedFileName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Archivo seleccionado") }
                )
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { launcher.launch("*/*") }) {
                    Text("Seleccionar archivo")
                }
                Button(
                    onClick = {
                        onClose()
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
    isExpense: Boolean,
    onInvoiceTypeChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterButton(
            text = "Ingresos",
            selected = !isExpense,
            onClick = {
                if (isExpense) {
                    onInvoiceTypeChange()
                }
            }
        )
        FilterButton(
            text = "Gastos",
            selected = isExpense,
            onClick = {
                if (!isExpense) {
                    onInvoiceTypeChange()
                }
            }
        )
    }
}

