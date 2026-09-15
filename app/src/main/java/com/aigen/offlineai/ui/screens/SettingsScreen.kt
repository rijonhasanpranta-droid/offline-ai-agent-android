package com.aigen.offlineai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aigen.aicore.config.ConfigManager
import com.aigen.offlineai.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val config = ConfigManager.getConfig()
    var temperature by remember { mutableStateOf(config.temperature) }
    var maxTokens by remember { mutableStateOf(config.maxTokens.toFloat()) }
    var enableMemory by remember { mutableStateOf(config.enableMemory) }
    var enableVoice by remember { mutableStateOf(config.enableVoice) }
    var streamTokens by remember { mutableStateOf(config.streamTokens) }
    var autoSave by remember { mutableStateOf(config.autoSaveConversations) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // Temperature setting
        Column {
            Text(
                text = "Temperature: %.2f".format(temperature),
                style = MaterialTheme.typography.labelMedium
            )
            Slider(
                value = temperature,
                onValueChange = { temperature = it },
                valueRange = 0f..2f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Controls randomness. 0.0 = deterministic, 2.0 = creative",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Max tokens setting
        Column {
            Text(
                text = "Max Tokens: ${maxTokens.toInt()}",
                style = MaterialTheme.typography.labelMedium
            )
            Slider(
                value = maxTokens,
                onValueChange = { maxTokens = it },
                valueRange = 1f..4096f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Maximum length of generated response",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Toggle settings
        SettingToggle(
            title = "Enable Memory",
            description = "Store information between conversations",
            checked = enableMemory,
            onCheckedChange = { enableMemory = it }
        )

        SettingToggle(
            title = "Enable Voice",
            description = "Use voice input and output",
            checked = enableVoice,
            onCheckedChange = { enableVoice = it }
        )

        SettingToggle(
            title = "Stream Tokens",
            description = "Show AI response as it's generated",
            checked = streamTokens,
            onCheckedChange = { streamTokens = it }
        )

        SettingToggle(
            title = "Auto-Save Conversations",
            description = "Automatically save chat history",
            checked = autoSave,
            onCheckedChange = { autoSave = it }
        )

        // Save button
        Button(
            onClick = {
                ConfigManager.updateTemperature(temperature)
                ConfigManager.updateMaxTokens(maxTokens.toInt())
                if (!enableMemory) ConfigManager.toggleMemory()
                if (!enableVoice) ConfigManager.toggleVoice()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Settings")
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
