package com.example.appproyectofindegradofranciscodasilva.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()


    Column(modifier = Modifier.fillMaxHeight()) {
        TextField(
            value = state.clientEmail,
            onValueChange = { viewModel.handleEvent(ChatEvent.OnClientEmailChange(it)) },
            label = { Text("Client Email") },
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = { viewModel.handleEvent(ChatEvent.LoadMessages) },
            enabled = !state.isLoading,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Load Messages")
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(state.messages) { message ->
                Text(
                    text = "${message.senderEmail}: ${message.content} - ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(message.timestamp)}",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .background(Color.LightGray)
                .fillMaxWidth()
        ) {
            TextField(
                value = state.message,
                onValueChange = { viewModel.handleEvent(ChatEvent.OnMessageTextChange(it)) },
                label = { Text("Message") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Button(
                onClick = { viewModel.handleEvent(ChatEvent.SendMessage) },
                enabled = !state.isLoading,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Send")
            }
        }
        state.errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(8.dp))
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }
    }
}