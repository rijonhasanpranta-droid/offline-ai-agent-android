package com.aigen.offlineai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Screen {
    CHAT, CONVERSATIONS, MEMORY, SETTINGS, MODEL_MANAGEMENT
}

@Composable
fun MainNavigation(
    chatViewModel: com.aigen.offlineai.viewmodel.ChatViewModel,
    conversationViewModel: com.aigen.offlineai.viewmodel.ConversationViewModel,
    memoryViewModel: com.aigen.offlineai.viewmodel.MemoryViewModel,
    modelViewModel: com.aigen.offlineai.viewmodel.ModelViewModel,
    settingsViewModel: com.aigen.offlineai.viewmodel.SettingsViewModel
) {
    var currentScreen by remember { mutableStateOf(Screen.CHAT) }
    var showDrawer by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentScreen) {
                        Screen.CHAT -> "Offline AI Agent"
                        Screen.CONVERSATIONS -> "Conversations"
                        Screen.MEMORY -> "Memory"
                        Screen.SETTINGS -> "Settings"
                        Screen.MODEL_MANAGEMENT -> "Models"
                    },
                    style = MaterialTheme.typography.headlineSmall
                )

                Row {
                    IconButton(onClick = { showDrawer = !showDrawer }) {
                        Icon(
                            imageVector = if (showDrawer) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showDrawer) {
                NavigationDrawer(
                    currentScreen = currentScreen,
                    onScreenSelected = {
                        currentScreen = it
                        showDrawer = false
                    }
                )
            } else {
                when (currentScreen) {
                    Screen.CHAT -> ChatScreen(chatViewModel)
                    Screen.CONVERSATIONS -> ConversationListScreen(
                        viewModel = conversationViewModel,
                        onConversationSelected = { /* Load conversation */ },
                        onNewConversation = { chatViewModel.clearConversation() }
                    )
                    Screen.MEMORY -> MemorySettingsScreen(memoryViewModel)
                    Screen.MODEL_MANAGEMENT -> ModelManagementScreen(modelViewModel)
                    Screen.SETTINGS -> SettingsScreen(settingsViewModel)
                }
            }
        }
    }
}

@Composable
fun NavigationDrawer(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Navigation",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Screen.values().forEach { screen ->
            Button(
                onClick = { onScreenSelected(screen) },
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        text = when (screen) {
                            Screen.CHAT -> "💬 Chat"
                            Screen.CONVERSATIONS -> "📚 Conversations"
                            Screen.MEMORY -> "🧠 Memory"
                            Screen.MODEL_MANAGEMENT -> "🤖 Models"
                            Screen.SETTINGS -> "⚙️ Settings"
                        }
                    )
                }
            )
        }
    }
}
