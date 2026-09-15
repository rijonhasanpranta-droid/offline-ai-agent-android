package com.aigen.offlineai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aigen.aicore.database.MemoryEntity
import com.aigen.offlineai.viewmodel.MemoryViewModel

@Composable
fun MemorySettingsScreen(viewModel: MemoryViewModel) {
    val allMemories by viewModel.allMemories.collectAsState()
    val memoryCount by viewModel.memoryCount.collectAsState(initial = 0)
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var selectedMemory by remember { mutableStateOf<MemoryEntity?>(null) }
    var editingMemory by remember { mutableStateOf<MemoryEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Memory Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Memory stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Memories: $memoryCount",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { showDeleteAllDialog = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Clear All")
            }
        }

        // Memories list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allMemories) { memory ->
                MemoryItem(
                    memory = memory,
                    onEdit = { editingMemory = memory },
                    onDelete = { viewModel.deleteMemory(memory.id) }
                )
            }
        }

        // Delete all dialog
        if (showDeleteAllDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteAllDialog = false },
                title = { Text("Clear All Memories?") },
                text = { Text("This action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearAllMemories()
                            showDeleteAllDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteAllDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Edit memory dialog
        if (editingMemory != null) {
            EditMemoryDialog(
                memory = editingMemory!!,
                onConfirm = { updatedContent ->
                    editingMemory = null
                },
                onDismiss = { editingMemory = null }
            )
        }
    }
}

@Composable
fun MemoryItem(
    memory: MemoryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memory.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = memory.content,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun EditMemoryDialog(
    memory: MemoryEntity,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var content by remember { mutableStateOf(memory.content) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Memory") },
        text = {
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Content") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(content) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
