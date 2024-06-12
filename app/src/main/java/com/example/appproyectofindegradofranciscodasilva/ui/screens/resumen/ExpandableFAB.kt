package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.appproyectofindegradofranciscodasilva.R

@Composable
fun ExpandableFloatingActionButton(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onClientesClick: () -> Unit,
    onContadoresClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space))
                ) {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_size_space)))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 1f))
                            .padding(dimensionResource(id = R.dimen.medium_size_space)),
                        text = "Clientes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_size_space)))
                    FloatingActionButton(
                        onClick = onClientesClick,
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Clientes")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space))
                ) {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_size_space)))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 1f))
                            .padding(dimensionResource(id = R.dimen.medium_size_space)),
                        text = "Contadores",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_size_space)))
                    FloatingActionButton(
                        onClick = onContadoresClick,
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Contadores")
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { onExpandChange(!expanded) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Expand/Collapse"
            )
        }
    }
}