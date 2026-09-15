package com.aigen.aicore.config

import android.content.Context
import androidx.datastore.preferences.core.stringPreferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore("app_config")

data class AppConfig(
    val modelPath: String = "/data/local/tmp/model.gguf",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 512,
    val contextLength: Int = 4096,
    val enableMemory: Boolean = true,
    val enableVoice: Boolean = true,
    val preferredLanguage: String = "en", // en or bn
    val streamTokens: Boolean = true,
    val autoSaveConversations: Boolean = true
)

object ConfigManager {
    private var config = AppConfig()

    fun getConfig(): AppConfig = config

    fun updateConfig(newConfig: AppConfig) {
        config = newConfig
    }

    fun updateTemperature(value: Float) {
        config = config.copy(temperature = value.coerceIn(0f, 2f))
    }

    fun updateMaxTokens(value: Int) {
        config = config.copy(maxTokens = value.coerceIn(1, 4096))
    }

    fun updateModelPath(path: String) {
        config = config.copy(modelPath = path)
    }

    fun toggleMemory() {
        config = config.copy(enableMemory = !config.enableMemory)
    }

    fun toggleVoice() {
        config = config.copy(enableVoice = !config.enableVoice)
    }
}
