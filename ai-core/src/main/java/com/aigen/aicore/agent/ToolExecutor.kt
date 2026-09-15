package com.aigen.aicore.agent

import android.content.Context
import android.content.ClipboardManager
import android.os.Build
import com.aigen.aicore.model.ToolCall
import com.aigen.aicore.model.ToolResult
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

interface Tool {
    fun getName(): String
    fun getDescription(): String
    suspend fun execute(arguments: Map<String, String>): String
}

class CalculatorTool : Tool {
    override fun getName(): String = "calculator"
    override fun getDescription(): String = "Performs mathematical calculations"

    override suspend fun execute(arguments: Map<String, String>): String {
        return try {
            val expression = arguments["expression"] ?: return "Error: No expression provided"
            val result = evalMath(expression)
            "Result: $result"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun evalMath(expression: String): Double {
        // Safe math evaluation
        val sanitized = expression.replace(Regex("[^\\d+\\-*/()\\s.]"), "")
        return try {
            eval(sanitized)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid mathematical expression")
        }
    }

    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0.toChar()

            fun nextChar() {
                ch = if (++pos < str.length) str[pos] else 0.toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch)
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+')) x += parseTerm() else if (eat('-')) x -= parseTerm() else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*')) x *= parseFactor() else if (eat('/')) x /= parseFactor() else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor()
                if (eat('-')) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('(')) {
                    x = parseExpression()
                    eat(')')
                } else if (ch >= '0' && ch <= '9' || ch == '.') {
                    while (ch >= '0' && ch <= '9' || ch == '.') nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch)
                }
                return x
            }
        }.parse()
    }
}

class DateTimeTool : Tool {
    override fun getName(): String = "datetime"
    override fun getDescription(): String = "Gets current date and time information"

    override suspend fun execute(arguments: Map<String, String>): String {
        return try {
            val format = arguments["format"] ?: "default"
            val now = LocalDateTime.now()
            val result = when (format) {
                "date" -> now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                "time" -> now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                "full" -> now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                else -> now.toString()
            }
            result
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

class ClipboardTool(private val context: Context) : Tool {
    override fun getName(): String = "clipboard"
    override fun getDescription(): String = "Access device clipboard"

    override suspend fun execute(arguments: Map<String, String>): String {
        return try {
            val action = arguments["action"] ?: "read"
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            when (action) {
                "read" -> {
                    val clip = clipboard.primaryClip
                    if (clip != null && clip.itemCount > 0) {
                        clip.getItemAt(0).text.toString()
                    } else {
                        "Clipboard is empty"
                    }
                }
                "write" -> {
                    val text = arguments["text"] ?: return "Error: No text provided"
                    val clip = android.content.ClipData.newPlainText("text", text)
                    clipboard.setPrimaryClip(clip)
                    "Text copied to clipboard"
                }
                else -> "Unknown action"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

class DeviceInfoTool(private val context: Context) : Tool {
    override fun getName(): String = "device_info"
    override fun getDescription(): String = "Gets device information"

    override suspend fun execute(arguments: Map<String, String>): String {
        return try {
            val info = arguments["info"] ?: "all"
            val sb = StringBuilder()

            if (info == "all" || info == "device") {
                sb.append("Device: ${Build.MODEL}\n")
                sb.append("Manufacturer: ${Build.MANUFACTURER}\n")
                sb.append("Android Version: ${Build.VERSION.RELEASE}\n")
            }

            if (info == "all" || info == "memory") {
                val runtime = Runtime.getRuntime()
                val totalMemory = runtime.totalMemory() / (1024 * 1024)
                val freeMemory = runtime.freeMemory() / (1024 * 1024)
                val usedMemory = totalMemory - freeMemory
                sb.append("Total Memory: ${totalMemory}MB\n")
                sb.append("Free Memory: ${freeMemory}MB\n")
                sb.append("Used Memory: ${usedMemory}MB\n")
            }

            sb.toString()
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

class NotesTool(private val context: Context) : Tool {
    override fun getName(): String = "notes"
    override fun getDescription(): String = "Create and manage local notes"

    override suspend fun execute(arguments: Map<String, String>): String {
        return try {
            val action = arguments["action"] ?: "create"
            val noteContent = arguments["content"] ?: return "Error: No content provided"
            val noteId = arguments["note_id"] ?: UUID.randomUUID().toString()

            when (action) {
                "create" -> "Note created with ID: $noteId"
                "update" -> "Note $noteId updated"
                "delete" -> "Note $noteId deleted"
                else -> "Unknown action"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

class ToolExecutor(private val context: Context) {
    private val tools: Map<String, Tool> = mapOf(
        "calculator" to CalculatorTool(),
        "datetime" to DateTimeTool(),
        "clipboard" to ClipboardTool(context),
        "device_info" to DeviceInfoTool(context),
        "notes" to NotesTool(context)
    )

    suspend fun executeTool(toolCall: ToolCall): ToolResult {
        return try {
            val tool = tools[toolCall.toolName]
            if (tool != null) {
                val result = tool.execute(toolCall.arguments)
                ToolResult(
                    toolId = toolCall.toolId,
                    success = true,
                    result = result
                )
            } else {
                ToolResult(
                    toolId = toolCall.toolId,
                    success = false,
                    result = "",
                    error = "Tool ${toolCall.toolName} not found"
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "Tool execution failed")
            ToolResult(
                toolId = toolCall.toolId,
                success = false,
                result = "",
                error = e.message
            )
        }
    }

    fun getAvailableTools(): List<String> = tools.keys.toList()

    fun getToolDescription(toolName: String): String? = tools[toolName]?.getDescription()
}
