package com.aigen.offlineai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pin
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
import com.aigen.aicore.database.ConversationEntity
import com.aigen.offlineai.viewmodel.ConversationViewModel

@Composable
fun ConversationListScreen(
    viewModel: ConversationViewModel,
    onConversationSelected: (String) -> Unit,
    onNewConversation: () -> Unit
) {
    val conversations by viewModel.allConversations.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var renamingId by remember { mutableStateOf<String?>(null) }
    var newTitle by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with New Chat button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Conversations",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onNewConversation) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("New Chat")
            }
        }

        // Search field
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search conversations...") },
            singleLine = true
        )

        // Conversations list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    isRenaming = renamingId == conversation.id,
                    newTitle = if (renamingId == conversation.id) newTitle else conversation.title,
                    onSelect = { onConversationSelected(conversation.id) },
                    onRename = { 
                        renamingId = conversation.id
                        newTitle = conversation.title
                    },
                    onConfirmRename = {
                        viewModel.renameConversation(conversation.id, newTitle)
                        renamingId = null
                    },
                    onCancel = { renamingId = null },
                    onTitleChange = { newTitle = it },
                    onDelete = { viewModel.deleteConversation(conversation.id) },
                    onPin = { viewModel.setPinned(conversation.id, !conversation.isPinned) }
                )
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: ConversationEntity,
    isRenaming: Boolean,
    newTitle: String,
    onSelect: () -> Unit,
    onRename: () -> Unit,
    onConfirmRename: () -> Unit,
    onCancel: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDelete: () -> Unit,
    onPin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isRenaming) { onSelect() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isRenaming) {
                TextField(
                    value = newTitle,
                    onValueChange = onTitleChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )
                Button(onClick = onConfirmRename) {
                    Text("Save")
                }
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
            } else {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = conversation.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Model: ${conversation.modelName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onPin) {
                    Icon(
                        imageVector = Icons.Default.Pin,
                        contentDescription = "Pin",
                        tint = if (conversation.isPinned) MaterialTheme.colorScheme.primary 
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onRename) {
                    Icon(Icons.Default.Edit, contentDescription = "Rename")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
