package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appproyectofindegradofranciscodasilva.R
import com.example.appproyectofindegradofranciscodasilva.ui.screens.login.LoginEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: ResumeViewModel = hiltViewModel()
){

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    
    Scaffold(
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
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.big_size_space))
        ){
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
                        onDismissRequest = {  viewModel.handleEvent(ResumenEvent.OnTrimesterMenuExpandedChanged) }
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
                    onExpandedChange = { viewModel.handleEvent(ResumenEvent.OnYearMenuExpandedChanged)}
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
            DualSegmentPieChart( data = mapOf(
                Pair("Ingresos", state.value.income),
                Pair("Gastos", state.value.expenses),
                ))
        }
    }
}
