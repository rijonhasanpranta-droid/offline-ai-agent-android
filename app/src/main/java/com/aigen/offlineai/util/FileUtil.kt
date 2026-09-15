package com.aigen.offlineai.util

import android.content.Context
import java.io.File

object FileUtil {
    fun getModelsDirectory(context: Context): File {
        val modelsDir = File(context.getExternalFilesDir(null), "models")
        if (!modelsDir.exists()) {
            modelsDir.mkdirs()
        }
        return modelsDir
    }

    fun getCacheDirectory(context: Context): File {
        val cacheDir = File(context.cacheDir, "ai_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir
    }

    fun getDataDirectory(context: Context): File {
        val dataDir = File(context.getExternalFilesDir(null), "data")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
        return dataDir
    }

    fun getAvailableSpace(context: Context): Long {
        return try {
            val stat = android.os.StatFs(context.getExternalFilesDir(null)?.absolutePath ?: "/")
            stat.availableBytes
        } catch (e: Exception) {
            0L
        }
    }

    fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> "%.2f GB".format(bytes / (1024.0 * 1024.0 * 1024.0))
            bytes >= 1024 * 1024 -> "%.2f MB".format(bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> "%.2f KB".format(bytes / 1024.0)
            else -> "$bytes B"
        }
    }
}
