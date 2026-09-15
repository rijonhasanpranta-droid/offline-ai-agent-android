package com.aigen.offlineai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aigen.aicore.llm.LLMManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ModelViewModel(private val context: Context) : ViewModel() {
    private val llmManager = LLMManager(context)

    private val _isModelLoaded = MutableStateFlow(false)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded.asStateFlow()

    private val _loadingProgress = MutableStateFlow(0f)
    val loadingProgress: StateFlow<Float> = _loadingProgress.asStateFlow()

    private val _engineInfo = MutableStateFlow("No engine loaded")
    val engineInfo: StateFlow<String> = _engineInfo.asStateFlow()

    init {
        _engineInfo.value = llmManager.getEngineInfo()
        _isModelLoaded.value = llmManager.isModelLoaded()
    }

    fun loadModel(modelPath: String) {
        viewModelScope.launch {
            try {
                Timber.d("Loading model: $modelPath")
                _loadingProgress.value = 0.1f

                val success = llmManager.loadModel(modelPath)
                
                _loadingProgress.value = 1f
                _isModelLoaded.value = success
                
                if (success) {
                    Timber.d("Model loaded successfully")
                    _engineInfo.value = llmManager.getEngineInfo()
                } else {
                    Timber.e("Failed to load model")
                }

                // Reset progress after a delay
                kotlinx.coroutines.delay(500)
                if (_loadingProgress.value == 1f) {
                    _loadingProgress.value = 0f
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading model")
                _loadingProgress.value = 0f
                _isModelLoaded.value = false
            }
        }
    }

    fun unloadModel() {
        viewModelScope.launch {
            try {
                llmManager.unloadModel()
                _isModelLoaded.value = false
                _engineInfo.value = "No engine loaded"
                Timber.d("Model unloaded")
            } catch (e: Exception) {
                Timber.e(e, "Error unloading model")
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ModelViewModel(context) as T
        }
    }
}
