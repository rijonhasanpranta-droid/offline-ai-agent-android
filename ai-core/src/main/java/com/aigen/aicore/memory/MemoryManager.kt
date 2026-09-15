package com.aigen.aicore.memory

import kotlinx.serialization.Serializable
import timber.log.Timber
import java.util.*

@Serializable
data class Memory(
    val id: String = UUID.randomUUID().toString(),
    val category: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)

class MemoryManager {
    private val memories = mutableListOf<Memory>()
    private val categories = mutableSetOf<String>()

    fun addMemory(category: String, content: String, metadata: Map<String, String> = emptyMap()): Memory {
        val memory = Memory(
            category = category,
            content = content,
            metadata = metadata
        )
        memories.add(memory)
        categories.add(category)
        Timber.d("Memory added: ${memory.id} in category: $category")
        return memory
    }

    fun getMemoriesByCategory(category: String): List<Memory> {
        return memories.filter { it.category == category }
    }

    fun getAllMemories(): List<Memory> = memories.toList()

    fun deleteMemory(memoryId: String): Boolean {
        val removed = memories.removeAll { it.id == memoryId }
        if (removed) {
            Timber.d("Memory deleted: $memoryId")
        }
        return removed
    }

    fun clearAllMemories() {
        memories.clear()
        Timber.d("All memories cleared")
    }

    fun searchMemories(query: String): List<Memory> {
        return memories.filter { memory ->
            memory.content.contains(query, ignoreCase = true) ||
            memory.category.contains(query, ignoreCase = true)
        }
    }

    fun getCategories(): Set<String> = categories.toSet()

    fun getMemoryCount(): Int = memories.size
}
