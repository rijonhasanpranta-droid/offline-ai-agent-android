package com.aigen.aicore.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromJsonString(value: String?): Map<String, String>? {
        return if (value == null) {
            null
        } else {
            try {
                Json.decodeFromString(value)
            } catch (e: Exception) {
                emptyMap()
            }
        }
    }

    @TypeConverter
    fun toJsonString(map: Map<String, String>?): String? {
        return if (map == null) {
            null
        } else {
            Json.encodeToString(map)
        }
    }
}
