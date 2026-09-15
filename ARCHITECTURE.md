# Architecture Overview

## System Design

```
┌─────────────────────────────────────────────────┐
│            Android App Layer (UI)               │
│  - Jetpack Compose UI Components                │
│  - Navigation & Routing                         │
│  - User Interactions & Events                   │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│         ViewModel Layer (MVVM)                  │
│  - ChatViewModel                                │
│  - ConversationViewModel                        │
│  - MemoryViewModel                              │
│  - ModelViewModel                               │
│  - SettingsViewModel                            │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│      Business Logic Layer (ai-core)             │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        LLM Management                    │  │
│  │  - LLMManager (coordinator)              │  │
│  │  - LlamaCppEngine                        │  │
│  │  - OnnxLLMEngine                         │  │
│  │  - Model Loading & Unloading             │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        Agent System                      │  │
│  │  - ToolExecutor                          │  │
│  │  - Calculator Tool                       │  │
│  │  - DateTime Tool                         │  │
│  │  - Clipboard Tool                        │  │
│  │  - DeviceInfo Tool                       │  │
│  │  - Notes Tool                            │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        Memory System                     │  │
│  │  - MemoryManager                         │  │
│  │  - Category-based Storage                │  │
│  │  - Search & Retrieval                    │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        Configuration Management          │  │
│  │  - ConfigManager                         │  │
│  │  - Runtime Settings                      │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│         Data Layer                              │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        Room Database                     │  │
│  │  - ConversationEntity                    │  │
│  │  - MessageEntity                         │  │
│  │  - MemoryEntity                          │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        File Storage                      │  │
│  │  - Models Directory                      │  │
│  │  - Cache Directory                       │  │
│  │  - Data Directory                        │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │        SharedPreferences                 │  │
│  │  - App Configuration                     │  │
│  │  - User Preferences                      │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│      Voice Processing Layer (voice-processor)   │
│  - SpeechToTextEngine                           │
│  - TextToSpeechEngine                           │
│  - WhisperSTTEngine                             │
│  - PiperTTSEngine                               │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│      Native Layer (JNI)                         │
│  - LLaMA.cpp Bindings                           │
│  - ONNX Runtime C++ API                         │
│  - Audio Processing Libraries                   │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│      Hardware & OS Layer                        │
│  - CPU/GPU                                      │
│  - RAM/Storage                                  │
│  - Microphone/Speaker                           │
│  - Sensors                                      │
└─────────────────────────────────────────────────┘
```

## Data Flow

### Chat Message Flow
```
1. User Input
   └─> ChatScreen (UI)
   └─> ChatViewModel.sendMessage()
   └─> Create UserMessage
   └─> Update messages state
   
2. AI Generation
   └─> LLMManager.generate()
   └─> LlamaCppEngine (or OnnxLLMEngine)
   └─> Token streaming via Flow
   └─> UpdateAssistantMessage incrementally
   
3. Persistence
   └─> Save to Room database
   └─> Update conversation metadata
   └─> Store in local storage
```

### Tool Usage Flow
```
1. AI Decision to Use Tool
   └─> LLM generates function call
   └─> Parse tool name & arguments
   
2. Tool Execution
   └─> ToolExecutor receives call
   └─> Lookup tool by name
   └─> Execute tool.execute(args)
   └─> Get result
   
3. Response Integration
   └─> Format tool result
   └─> Include in next prompt
   └─> Continue generation
```

## State Management

### ViewModel State
```kotlin
// ChatViewModel uses StateFlow for:
- messages: StateFlow<List<Message>>
- isGenerating: StateFlow<Boolean>
- currentConversationId: StateFlow<String>
- lastError: StateFlow<String?>

// Updates propagate to UI via:
val messages by viewModel.messages.collectAsState()
```

### Offline-First Principle
```
All operations are local-first:
- Messages saved to DB immediately
- Inference happens on-device
- No network calls required
- Network availability is informational only
```

## Error Handling

```
┌─ Model Not Loaded ──────────────────┐
│  Show error message                  │
│  Prompt user to load model           │
│  Provide model management UI         │
└──────────────────────────────────────┘

┌─ Insufficient RAM ──────────────────┐
│  Check available memory              │
│  Warn user before loading            │
│  Suggest smaller quantization        │
└──────────────────────────────────────┘

┌─ Generation Error ──────────────────┐
│  Log error for debugging             │
│  Show user-friendly message          │
│  Allow retry or cancel               │
└──────────────────────────────────────┘

┌─ Database Error ────────────────────┐
│  Fallback to in-memory storage       │
│  Log for debugging                   │
│  Notify user of data risk            │
└──────────────────────────────────────┘
```

## Concurrency Model

### Coroutines Usage
```kotlin
// UI Thread (Main)
- User interactions
- UI state updates
- Compose recompositions

// IO Thread (Room, File I/O)
- Database operations
- File reading/writing
- SharedPreferences

// Default Thread (Heavy computation)
- Model inference (moved to native threads)
- Text processing
- Memory operations

// Custom Dispatcher (if needed)
- Large tensor operations
- Parallel processing
```

## Security Considerations

### Data Protection
- All user data stored locally
- No external network requests for core features
- Encrypted SharedPreferences (recommended)
- Secure file permissions

### Memory Safety
- JNI bindings validated
- Buffer overflow protection
- Proper native resource cleanup

### Permission Model
- Minimal permissions requested
- Runtime permissions for sensitive features
- User transparency in permission prompts

---

For detailed implementation guidance, see IMPLEMENTATION_GUIDE.md
