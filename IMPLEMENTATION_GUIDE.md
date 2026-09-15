# Implementation Guide - Offline AI Agent Android

## Quick Start Development

### 1. LLM Engine Implementation

#### LLaMA.cpp Integration

```kotlin
// In LlamaCppEngine.kt
private val llamaCppSession: LlamaService // JNI binding

override suspend fun generate(
    prompt: String,
    temperature: Float,
    maxTokens: Int,
    topP: Float,
    topK: Int
): Flow<String> = flow {
    val tokens = llamaCppSession.tokenize(prompt)
    var generatedTokens = 0
    
    while (generatedTokens < maxTokens) {
        val logits = llamaCppSession.eval(tokens)
        val nextToken = sampleToken(
            logits,
            temperature,
            topP,
            topK
        )
        
        val tokenStr = llamaCppSession.tokenToPiece(nextToken)
        emit(tokenStr)
        
        tokens.add(nextToken)
        generatedTokens++
        
        if (nextToken == llamaCppSession.getEosToken()) {
            break
        }
    }
}
```

#### ONNX Runtime Integration

```kotlin
// In OnnxLLMEngine.kt
private lateinit var session: OrtSession

override suspend fun initialize(modelPath: String): Boolean {
    return try {
        val env = OrtEnvironment.getEnvironment()
        val sessionOptions = OrtSession.SessionOptions().apply {
            // Enable GPU if available
            // addCoreMLExecutionProvider()
            // addCudaExecutionProvider()
        }
        session = env.createSession(modelPath, sessionOptions)
        isModelLoaded = true
        true
    } catch (e: Exception) {
        Timber.e(e, "ONNX initialization failed")
        false
    }
}
```

### 2. Database Setup

```kotlin
// In MainActivity.kt
val db = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "offline_ai_db"
).addMigrations(/* migrations */)
 .build()

// Use dependency injection (Hilt recommended)
@Provides
@Singleton
fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "offline_ai_db"
    ).build()
}
```

### 3. Chat Message Streaming

```kotlin
// In ChatViewModel.kt
fun sendMessage(text: String) {
    viewModelScope.launch {
        val userMessage = Message(..., role = USER)
        addMessage(userMessage)
        
        val assistantMessage = Message(..., role = ASSISTANT, isStreaming = true)
        addMessage(assistantMessage)
        
        llmManager.generate(text).collect { token ->
            updateLastMessageContent { it + token }
        }
    }
}

private fun updateLastMessageContent(transform: (String) -> String) {
    _messages.value = _messages.value.dropLast(1) + 
        _messages.value.last().copy(
            content = transform(_messages.value.last().content)
        )
}
```

### 4. Voice Processing

```kotlin
// In VoiceProcessor.kt
suspend fun recordAndTranscribe(language: String = "en"): String {
    val audioFile = File(context.cacheDir, "recording.wav")
    
    // Record audio
    val mediaRecorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setOutputFile(audioFile)
        prepare()
        start()
    }
    
    // Wait for recording
    delay(5000) // 5 second recording
    
    mediaRecorder.stop()
    mediaRecorder.release()
    
    // Transcribe using Whisper.cpp
    return whisperEngine.transcribe(audioFile.absolutePath, language)
}
```

### 5. Tool Integration

```kotlin
// Example: Extend Calculator Tool
class AdvancedCalculatorTool : Tool {
    override fun getName(): String = "advanced_calculator"
    
    override suspend fun execute(arguments: Map<String, String>): String {
        val expression = arguments["expression"] ?: return "Error: No expression"
        val mode = arguments["mode"] ?: "basic" // basic, scientific, matrix
        
        return when (mode) {
            "scientific" -> evaluateScientific(expression)
            "matrix" -> evaluateMatrix(expression)
            else -> evaluateMath(expression)
        }
    }
}
```

### 6. Memory Integration

```kotlin
// In ChatViewModel.kt
fun processResponseWithMemory(response: String) {
    // Extract memorable information
    val memories = extractMemories(response)
    
    memories.forEach { (category, content) ->
        memoryManager.addMemory(category, content)
    }
}

private fun extractMemories(text: String): List<Pair<String, String>> {
    // Use regex or NLP to extract facts
    // e.g., dates, names, preferences
    return listOf()
}
```

## Testing Strategy

### Unit Tests

```kotlin
class CalculatorToolTest {
    @Test
    fun testSimpleAddition() = runTest {
        val tool = CalculatorTool()
        val result = tool.execute(mapOf("expression" to "2+2"))
        assertTrue(result.contains("4"))
    }
}
```

### Integration Tests

```kotlin
class ChatViewModelTest {
    @Test
    fun testMessageFlow() = runTest {
        val viewModel = ChatViewModel(context)
        viewModel.sendMessage("Hello")
        
        advanceUntilIdle()
        assertTrue(viewModel.messages.value.isNotEmpty())
    }
}
```

## Performance Optimization

### Model Quantization
- Use Q4_K_M quantization for 7B models (~4GB)
- Use Q2_K quantization for very limited devices (~2GB)
- Profile with Android Profiler

### Memory Management
```kotlin
// In LLMManager.kt
override fun onCleared() {
    super.onCleared()
    llmManager.unloadModel() // Free memory
    System.gc() // Suggest garbage collection
}
```

### Caching Strategy
- Cache tokenization results
- Use Room for conversation caching
- Implement LRU cache for tokens

## Deployment Checklist

- [ ] Minify code with R8/ProGuard
- [ ] Test on API 26, 29, 31, 34
- [ ] Test on devices with 2GB, 4GB, 6GB RAM
- [ ] Verify offline functionality
- [ ] Test voice features (if implemented)
- [ ] Check battery consumption
- [ ] Verify storage requirements
- [ ] Security review
- [ ] Performance profiling

## Common Issues & Solutions

### Issue: Model fails to load
**Solution**: Check file path, verify available RAM, check file permissions

### Issue: Slow inference
**Solution**: Use smaller quantization, reduce context length, enable GPU

### Issue: Voice recognition not working
**Solution**: Check Whisper model loading, verify microphone permission

### Issue: UI freezes during generation
**Solution**: Ensure generation happens on background coroutine, use StateFlow for updates

## Next Steps

1. Implement LLaMA.cpp JNI wrapper
2. Complete streaming UI rendering
3. Add voice input/output
4. Optimize performance for target devices
5. Implement comprehensive testing
6. Add analytics (privacy-preserving)
7. Create model download manager
8. Add multi-language support improvements

## Resources

- [LLaMA.cpp Docs](https://github.com/ggerganov/llama.cpp)
- [Android ONNX Runtime](https://onnxruntime.ai/docs/build/android.html)
- [Jetpack Compose Performance](https://developer.android.com/jetpack/compose/performance)
- [Room Best Practices](https://developer.android.com/training/data-storage/room)

---

For questions or issues, open a GitHub issue or discussion.
