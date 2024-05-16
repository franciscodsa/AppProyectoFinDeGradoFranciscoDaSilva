package com.example.appproyectofindegradofranciscodasilva.ui.screens.archivos

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.File

@Composable
fun FileSelectionScreen(
    viewModel: FileViewModel = hiltViewModel(),
    onFileSelected: (File) -> Unit
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            result?.let {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(it)?:""
                val selectedFile = File(context.cacheDir, it.lastPathSegment?:"") // You can use any desired file name here


                contentResolver.openInputStream(it)?.use { inputStream ->
                    selectedFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                onFileSelected(selectedFile)
                viewModel.handleEvent(FileEvent.OnMimeTypeSelected(mimeType))
                viewModel.handleEvent(FileEvent.OnFileSelected(selectedFile))
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch("*/*") }) {
            Text("Seleccionar archivo")
        }
        Button(onClick = { viewModel.handleEvent(FileEvent.UploadFile) }) {
            Text(text = "Subir")
        }
    }
}
