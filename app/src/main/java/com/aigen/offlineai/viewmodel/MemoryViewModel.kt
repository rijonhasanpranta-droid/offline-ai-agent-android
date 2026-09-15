package com.aigen.offlineai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aigen.aicore.database.MemoryEntity
import com.aigen.aicore.database.AppDatabase
import androidx.room.Room
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MemoryViewModel(private val context: Context) : ViewModel() {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "offline_ai_db"
    ).build()

    private val memoryDao = db.memoryDao()

    val allMemories: StateFlow<List<MemoryEntity>> = 
        memoryDao.getAllMemories()
            .stateIn(viewModelScope, androidx.lifecycle.viewmodel.viewmodel, emptyList())

    val memoryCount: StateFlow<Int> = 
        memoryDao.getMemoryCount()
            .stateIn(viewModelScope, androidx.lifecycle.viewmodel.viewmodel, 0)

    fun deleteMemory(memoryId: String) {
        viewModelScope.launch {
            try {
                memoryDao.deleteMemoryById(memoryId)
                Timber.d("Memory deleted: $memoryId")
            } catch (e: Exception) {
                Timber.e(e, "Failed to delete memory")
            }
        }
    }

    fun clearAllMemories() {
        viewModelScope.launch {
            try {
                memoryDao.deleteAllMemories()
                Timber.d("All memories cleared")
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear memories")
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MemoryViewModel(context) as T
        }
    }
}
