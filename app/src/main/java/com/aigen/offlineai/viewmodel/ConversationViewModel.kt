package com.aigen.offlineai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aigen.aicore.database.ConversationEntity
import com.aigen.aicore.database.AppDatabase
import androidx.room.Room
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class ConversationViewModel(private val context: Context) : ViewModel() {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "offline_ai_db"
    ).build()

    private val conversationDao = db.conversationDao()

    val allConversations: StateFlow<List<ConversationEntity>> = 
        conversationDao.getAllConversations()
            .stateIn(viewModelScope, androidx.lifecycle.viewmodel.viewmodel, emptyList())

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            try {
                conversationDao.deleteById(conversationId)
                Timber.d("Conversation deleted: $conversationId")
            } catch (e: Exception) {
                Timber.e(e, "Failed to delete conversation")
            }
        }
    }

    fun renameConversation(conversationId: String, newTitle: String) {
        viewModelScope.launch {
            try {
                conversationDao.renameConversation(conversationId, newTitle)
                Timber.d("Conversation renamed: $conversationId")
            } catch (e: Exception) {
                Timber.e(e, "Failed to rename conversation")
            }
        }
    }

    fun setPinned(conversationId: String, isPinned: Boolean) {
        viewModelScope.launch {
            try {
                conversationDao.setPinned(conversationId, isPinned)
                Timber.d("Conversation pin status updated: $conversationId")
            } catch (e: Exception) {
                Timber.e(e, "Failed to update pin status")
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ConversationViewModel(context) as T
        }
    }
}
