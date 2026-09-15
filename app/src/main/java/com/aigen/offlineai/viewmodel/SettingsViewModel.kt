package com.aigen.offlineai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aigen.aicore.config.ConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class SettingsViewModel : ViewModel() {
    private val _config = MutableStateFlow(ConfigManager.getConfig())
    val config: StateFlow<com.aigen.aicore.config.AppConfig> = _config

    fun updateTemperature(value: Float) {
        try {
            ConfigManager.updateTemperature(value)
            _config.value = ConfigManager.getConfig()
            Timber.d("Temperature updated to: $value")
        } catch (e: Exception) {
            Timber.e(e, "Failed to update temperature")
        }
    }

    fun updateMaxTokens(value: Int) {
        try {
            ConfigManager.updateMaxTokens(value)
            _config.value = ConfigManager.getConfig()
            Timber.d("Max tokens updated to: $value")
        } catch (e: Exception) {
            Timber.e(e, "Failed to update max tokens")
        }
    }

    fun toggleMemory() {
        try {
            ConfigManager.toggleMemory()
            _config.value = ConfigManager.getConfig()
            Timber.d("Memory toggled")
        } catch (e: Exception) {
            Timber.e(e, "Failed to toggle memory")
        }
    }

    fun toggleVoice() {
        try {
            ConfigManager.toggleVoice()
            _config.value = ConfigManager.getConfig()
            Timber.d("Voice toggled")
        } catch (e: Exception) {
            Timber.e(e, "Failed to toggle voice")
        }
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel() as T
        }
    }
}
