package com.aigen.aicore.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

@Entity(tableName = "conversations")
@Serializable
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val modelName: String,
    val modelPath: String,
    val contextLength: Int,
    val isPinned: Boolean = false
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId")]
)
@Serializable
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val content: String,
    val role: String, // USER or ASSISTANT
    val timestamp: Long,
    val isStreaming: Boolean = false,
    val messageOrder: Int
)

@Entity(
    tableName = "memories",
    indices = [Index("category")]
)
@Serializable
data class MemoryEntity(
    @PrimaryKey
    val id: String,
    val category: String,
    val content: String,
    val timestamp: Long,
    val metadata: String // JSON serialized
)
