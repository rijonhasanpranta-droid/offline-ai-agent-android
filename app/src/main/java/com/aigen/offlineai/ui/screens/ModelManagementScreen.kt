package com.aigen.offlineai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.aigen.offlineai.viewmodel.ModelViewModel

@Composable
fun ModelManagementScreen(viewModel: ModelViewModel) {
    val isModelLoaded by viewModel.isModelLoaded.collectAsState()
    val modelLoadingProgress by viewModel.loadingProgress.collectAsState()
    val engineInfo by viewModel.engineInfo.collectAsState()
    var modelPath by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Model Management",
            style = MaterialTheme.typography.headlineSmall
        )

        // Model status card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Model Status",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isModelLoaded) "Loaded" else "Not Loaded",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isModelLoaded) MaterialTheme.colorScheme.primary 
                               else MaterialTheme.colorScheme.error
                    )
                    if (!isModelLoaded) {
                        Text(
                            text = "Ready to load",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Engine info card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Engine Information",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = engineInfo,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Model path input
        TextField(
            value = modelPath,
            onValueChange = { modelPath = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Model Path") },
            placeholder = { Text("/path/to/model.gguf") },
            singleLine = true,
            enabled = !isModelLoaded
        )

        // Loading progress
        if (modelLoadingProgress > 0f && modelLoadingProgress < 1f) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Loading: ${(modelLoadingProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
                LinearProgressIndicator(
                    progress = modelLoadingProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Load/Unload buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isModelLoaded) {
                Button(
                    onClick = { viewModel.unloadModel() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Text("Unload Model")
                }
            } else {
                Button(
                    onClick = { viewModel.loadModel(modelPath) },
                    modifier = Modifier.weight(1f),
                    enabled = modelPath.isNotBlank()
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Text("Load Model")
                }
            }
        }

        // Model recommendations
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Recommended Models",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "• Phi-2 (2.7B) - Fast and capable\n" +
                            "• TinyLlama (1.1B) - Very lightweight\n" +
                            "• OpenHermes (7B) - Feature rich\n" +
                            "• Mistral (7B) - Balanced performance",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
