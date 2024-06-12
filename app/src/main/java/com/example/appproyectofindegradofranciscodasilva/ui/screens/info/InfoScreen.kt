package com.example.appproyectofindegradofranciscodasilva.ui.screens.info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appproyectofindegradofranciscodasilva.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(bottomNavigationBar: @Composable () -> Unit = {}) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información") }
            )
        },
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.big_size_space)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "En el siguiente enlace, podrás encontrar tutoriales:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_size_space)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_youtube),
                    contentDescription = "YouTube Icon",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.bigger_size_space))
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_size_space)))

                ClickableText(
                    text = AnnotatedString("Ver Tutoriales"),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/playlist?list=PLYMGxStQDhPZDX5xgqmp0tY--AqSvReur")
                        )
                        context.startActivity(intent)
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_size_space)))

            Text(
                text = "Recuerda que siempre puedes preguntar cualquier duda que tengas a tu contador usando el chat",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
