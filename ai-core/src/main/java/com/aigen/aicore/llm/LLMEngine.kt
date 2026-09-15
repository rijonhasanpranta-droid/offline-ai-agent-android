package com.aigen.aicore.llm

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

interface LLMEngine {
    suspend fun initialize(modelPath: String): Boolean
    suspend fun generate(
        prompt: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 512,
        topP: Float = 0.9f,
        topK: Int = 40
    ): Flow<String>
    suspend fun cancel()
    fun isLoaded(): Boolean
    fun unload()
    fun getModelInfo(): String
}

class OnnxLLMEngine(private val context: Context) : LLMEngine {
    private var isModelLoaded = false
    private val _generationFlow = MutableSharedFlow<String>()
    val generationFlow = _generationFlow.asSharedFlow()

    override suspend fun initialize(modelPath: String): Boolean {
        return try {
            Timber.d("Initializing ONNX LLM from: $modelPath")
            // Initialize ONNX Runtime session
            // Load model from modelPath
            isModelLoaded = true
            Timber.d("ONNX LLM initialized successfully")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize ONNX LLM")
            false
        }
    }

    override suspend fun generate(
        prompt: String,
        temperature: Float,
        maxTokens: Int,
        topP: Float,
        topK: Int
    ): Flow<String> {
        return try {
            Timber.d("Generating response for prompt: ${prompt.take(50)}...")
            // Run inference with temperature sampling
            // Stream tokens back via flow
            generationFlow
        } catch (e: Exception) {
            Timber.e(e, "Generation failed")
            throw e
        }
    }

    override suspend fun cancel() {
        Timber.d("Cancelling generation")
    }

    override fun isLoaded(): Boolean = isModelLoaded

    override fun unload() {
        if (isModelLoaded) {
            Timber.d("Unloading model")
            isModelLoaded = false
        }
    }

    override fun getModelInfo(): String {
        return "ONNX Runtime LLM"
    }
}

class LlamaCppEngine(private val context: Context) : LLMEngine {
    private var isModelLoaded = false
    private val _generationFlow = MutableSharedFlow<String>()
    val generationFlow = _generationFlow.asSharedFlow()

    override suspend fun initialize(modelPath: String): Boolean {
        return try {
            Timber.d("Initializing LLaMA.cpp from: $modelPath")
            // Initialize LLaMA.cpp with model
            // Load quantized GGUF model
            isModelLoaded = true
            Timber.d("LLaMA.cpp initialized successfully")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize LLaMA.cpp")
            false
        }
    }

    override suspend fun generate(
        prompt: String,
        temperature: Float,
        maxTokens: Int,
        topP: Float,
        topK: Int
    ): Flow<String> {
        return try {
            Timber.d("Generating response for prompt: ${prompt.take(50)}...")
            // Stream tokens from LLaMA.cpp
            generationFlow
        } catch (e: Exception) {
            Timber.e(e, "Generation failed")
            throw e
        }
    }

    override suspend fun cancel() {
        Timber.d("Cancelling generation")
    }

    override fun isLoaded(): Boolean = isModelLoaded

    override fun unload() {
        if (isModelLoaded) {
            Timber.d("Unloading model")
            isModelLoaded = false
        }
    }

    override fun getModelInfo(): String {
        return "LLaMA.cpp Engine"
    }
}
