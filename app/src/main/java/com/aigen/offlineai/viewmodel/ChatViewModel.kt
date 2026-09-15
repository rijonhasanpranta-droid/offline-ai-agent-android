package com.aigen.offlineai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aigen.aicore.llm.LLMManager
import com.aigen.aicore.model.Message
import com.aigen.aicore.model.MessageRole
import com.aigen.aicore.agent.ToolExecutor
import com.aigen.aicore.memory.MemoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ChatViewModel(private val context: Context) : ViewModel() {
    private val llmManager = LLMManager(context)
    private val toolExecutor = ToolExecutor(context)
    private val memoryManager = MemoryManager()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentConversationId = MutableStateFlow(UUID.randomUUID().toString())
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    init {
        Timber.d("ChatViewModel initialized")
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                // Add user message
                val userMessage = Message(
                    id = UUID.randomUUID().toString(),
                    content = text,
                    role = MessageRole.USER,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + userMessage

                _isGenerating.value = true

                // Generate AI response
                val assistantMessage = Message(
                    id = UUID.randomUUID().toString(),
                    content = "",
                    role = MessageRole.ASSISTANT,
                    timestamp = System.currentTimeMillis(),
                    isStreaming = true
                )
                _messages.value = _messages.value + assistantMessage

                // Get response from LLM
                if (llmManager.isModelLoaded()) {
                    llmManager.generate(text).collect { token ->
                        val updatedMessage = assistantMessage.copy(
                            content = assistantMessage.content + token
                        )
                        updateLastMessage(updatedMessage)
                    }
                } else {
                    updateLastMessage(assistantMessage.copy(
                        content = "Model not loaded. Please load a model first.",
                        isStreaming = false
                    ))
                }

                _isGenerating.value = false
            } catch (e: Exception) {
                Timber.e(e, "Error sending message")
                _isGenerating.value = false
            }
        }
    }

    private fun updateLastMessage(message: Message) {
        val currentMessages = _messages.value.toMutableList()
        if (currentMessages.isNotEmpty()) {
            currentMessages[currentMessages.size - 1] = message
            _messages.value = currentMessages
        }
    }

    fun loadModel(modelPath: String) {
        viewModelScope.launch {
            try {
                _isGenerating.value = true
                val success = llmManager.loadModel(modelPath)
                if (success) {
                    Timber.d("Model loaded successfully")
                } else {
                    Timber.e("Failed to load model")
                }
                _isGenerating.value = false
            } catch (e: Exception) {
                Timber.e(e, "Error loading model")
                _isGenerating.value = false
            }
        }
    }

    fun clearConversation() {
        _messages.value = emptyList()
        _currentConversationId.value = UUID.randomUUID().toString()
    }

    fun deleteMessage(messageId: String) {
        _messages.value = _messages.value.filter { it.id != messageId }
    }

    fun editMessage(messageId: String, newContent: String) {
        _messages.value = _messages.value.map {
            if (it.id == messageId) it.copy(content = newContent) else it
        }
    }

    fun addMemory(category: String, content: String) {
        memoryManager.addMemory(category, content)
    }

    override fun onCleared() {
        super.onCleared()
        llmManager.unloadModel()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(context) as T
        }
    }
}
