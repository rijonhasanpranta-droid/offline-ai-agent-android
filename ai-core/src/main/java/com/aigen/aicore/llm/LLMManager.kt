package com.aigen.aicore.llm

import android.content.Context
import com.aigen.aicore.config.ConfigManager
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.io.File

class LLMManager(private val context: Context) {
    private var engine: LLMEngine? = null
    private var currentModelPath: String? = null

    suspend fun loadModel(modelPath: String, engineType: EngineType = EngineType.LLAMA_CPP): Boolean {
        return try {
            Timber.d("Loading model from: $modelPath")

            // Validate model file exists
            val modelFile = File(modelPath)
            if (!modelFile.exists()) {
                Timber.e("Model file not found: $modelPath")
                return false
            }

            // Check available RAM
            val runtime = Runtime.getRuntime()
            val freeMemory = runtime.freeMemory() / (1024 * 1024) // Convert to MB
            Timber.d("Available RAM: ${freeMemory}MB")

            if (freeMemory < MIN_MEMORY_MB) {
                Timber.e("Insufficient RAM. Required: ${MIN_MEMORY_MB}MB, Available: ${freeMemory}MB")
                return false
            }

            // Create engine instance
            engine = when (engineType) {
                EngineType.ONNX -> OnnxLLMEngine(context)
                EngineType.LLAMA_CPP -> LlamaCppEngine(context)
            }

            // Initialize engine
            val success = engine?.initialize(modelPath) ?: false
            if (success) {
                currentModelPath = modelPath
                ConfigManager.updateModelPath(modelPath)
                Timber.d("Model loaded successfully")
            }
            success
        } catch (e: Exception) {
            Timber.e(e, "Failed to load model")
            false
        }
    }

    suspend fun generate(
        prompt: String,
        temperature: Float = ConfigManager.getConfig().temperature,
        maxTokens: Int = ConfigManager.getConfig().maxTokens,
        topP: Float = 0.9f,
        topK: Int = 40
    ): Flow<String> {
        return if (engine?.isLoaded() == true) {
            engine!!.generate(prompt, temperature, maxTokens, topP, topK)
        } else {
            throw IllegalStateException("Model not loaded")
        }
    }

    fun isModelLoaded(): Boolean = engine?.isLoaded() == true

    suspend fun cancelGeneration() {
        engine?.cancel()
    }

    fun unloadModel() {
        engine?.unload()
        engine = null
        currentModelPath = null
        Timber.d("Model unloaded")
    }

    fun getCurrentModelPath(): String? = currentModelPath

    fun getEngineInfo(): String = engine?.getModelInfo() ?: "No engine loaded"

    companion object {
        private const val MIN_MEMORY_MB = 2048 // 2GB minimum
    }
}

enum class EngineType {
    ONNX, LLAMA_CPP
}
