package com.aigen.aicore.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val content: String,
    val role: MessageRole,
    val timestamp: Long,
    val isStreaming: Boolean = false,
    val canEdit: Boolean = role == MessageRole.USER,
    val canDelete: Boolean = true
)

@Serializable
enum class MessageRole {
    USER, ASSISTANT, SYSTEM
}

@Serializable
data class Conversation(
    val id: String,
    val title: String,
    val messages: List<Message>,
    val createdAt: Long,
    val updatedAt: Long,
    val modelInfo: ModelInfo,
    val isPinned: Boolean = false
)

@Serializable
data class ModelInfo(
    val name: String,
    val modelPath: String,
    val contextLength: Int,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 512,
    val topP: Float = 0.9f,
    val topK: Int = 40,
    val quantization: String = "Q4_K_M"
)

@Serializable
data class ToolCall(
    val toolName: String,
    val toolId: String,
    val arguments: Map<String, String>,
    val timestamp: Long
)

@Serializable
data class ToolResult(
    val toolId: String,
    val success: Boolean,
    val result: String,
    val error: String? = null
)
