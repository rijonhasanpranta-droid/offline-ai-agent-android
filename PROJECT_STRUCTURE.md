# Offline AI Agent - Project Structure Reference

## Complete Directory Tree

```
offline-ai-agent-android/
│
├── 📁 app/                                 # Main Android App Module
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── AndroidManifest.xml        # App permissions & configuration
│   │   │   ├── 📁 java/com/aigen/offlineai/
│   │   │   │   ├── MainActivity.kt        # App entry point
│   │   │   │   ├── BuildConfig.kt         # Build configuration
│   │   │   │   ├── 📁 ui/
│   │   │   │   │   ├── 📁 screens/        # Compose screens
│   │   │   │   │   │   ├── ChatScreen.kt
│   │   │   │   │   │   ├── ConversationListScreen.kt
│   │   │   │   │   │   ├── MemorySettingsScreen.kt
│   │   │   │   │   │   ├── ModelManagementScreen.kt
│   │   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   │   ├── 📁 components/     # Reusable UI components
│   │   │   │   │   │   └── ChatMessage.kt
│   │   │   │   │   ├── 📁 navigation/     # Navigation & routing
│   │   │   │   │   │   └── Navigation.kt
│   │   │   │   │   └── 📁 theme/          # Material Design theme
│   │   │   │   │       └── Theme.kt
│   │   │   │   ├── 📁 viewmodel/          # MVVM ViewModels
│   │   │   │   │   ├── ChatViewModel.kt
│   │   │   │   │   ├── ConversationViewModel.kt
│   │   │   │   │   ├── MemoryViewModel.kt
│   │   │   │   │   ├── ModelViewModel.kt
│   │   │   │   │   └── SettingsViewModel.kt
│   │   │   │   └── 📁 util/               # Utility classes
│   │   │   │       ├── FileUtil.kt        # File management
│   │   │   │       ├── NetworkUtil.kt     # Network detection
│   │   │   │       ├── PermissionUtil.kt  # Permission handling
│   │   │   │       └── PreferenceManager.kt # SharedPreferences
│   │   │   ├── 📁 res/
│   │   │   │   ├── 📁 values/
│   │   │   │   │   ├── strings.xml        # UI strings
│   │   │   │   │   ├── strings-extended.xml  # Extended strings
│   │   │   │   │   ├── colors.xml         # Light theme colors
│   │   │   │   │   └── arrays.xml         # Array resources
│   │   │   │   ├── 📁 values-night/       # Dark theme
│   │   │   │   │   └── colors.xml
│   │   │   │   ├── 📁 drawable/           # Vector drawables
│   │   │   │   ├── 📁 layout/             # Legacy XML layouts (if any)
│   │   │   │   ├── 📁 mipmap/             # App icons
│   │   │   │   ├── 📁 menu/               # Menu resources
│   │   │   │   └── 📁 xml/
│   │   │   │       ├── data_extraction_rules.xml
│   │   │   │       └── backup_rules.xml
│   │   ├── 📁 test/
│   │   │   └── 📁 java/com/aigen/offlineai/
│   │   │       ├── ChatViewModelTest.kt
│   │   │       ├── CalculatorToolTest.kt
│   │   │       └── MemoryManagerTest.kt
│   │   └── 📁 androidTest/
│   │       └── 📁 java/com/aigen/offlineai/
│   │           ├── ChatScreenTest.kt
│   │           └── DatabaseTest.kt
│   ├── build.gradle.kts                  # App module build config
│   └── proguard-rules.pro                # ProGuard obfuscation
│
├── 📁 ai-core/                            # AI & LLM Core Module
│   ├── 📁 src/main/java/com/aigen/aicore/
│   │   ├── 📁 llm/                       # LLM Management
│   │   │   ├── LLMEngine.kt              # Interface for LLM engines
│   │   │   ├── LlamaCppEngine.kt         # LLaMA.cpp implementation
│   │   │   ├── OnnxLLMEngine.kt          # ONNX Runtime implementation
│   │   │   └── LLMManager.kt             # LLM manager/coordinator
│   │   ├── 📁 agent/                     # Agent & Tools
│   │   │   ├── ToolExecutor.kt           # Tool execution engine
│   │   │   ├── CalculatorTool.kt         # Math evaluation
│   │   │   ├── DateTimeTool.kt           # Date/time retrieval
│   │   │   ├── ClipboardTool.kt          # Clipboard access
│   │   │   ├── DeviceInfoTool.kt         # Device information
│   │   │   └── NotesTool.kt              # Local note management
│   │   ├── 📁 memory/                    # Memory Management
│   │   │   └── MemoryManager.kt          # In-memory & persistent storage
│   │   ├��─ 📁 database/                  # Room Database
│   │   │   ├── AppDatabase.kt            # Database definition
│   │   │   ├── Entities.kt               # Data entities
│   │   │   │   ├── ConversationEntity
│   │   │   │   ├── MessageEntity
│   │   │   │   └── MemoryEntity
│   │   │   ├── Daos.kt                   # Data Access Objects
│   │   │   │   ├── ConversationDao
│   │   │   │   ├── MessageDao
│   │   │   │   └── MemoryDao
│   │   │   └── Converters.kt             # Type converters
│   │   ├── 📁 model/                     # Data Models
│   │   │   └── Models.kt
│   │   │       ├── Message
│   │   │       ├── Conversation
│   │   │       ├── ModelInfo
│   │   │       ├── ToolCall
│   │   │       └── ToolResult
│   │   └── 📁 config/                    # Configuration
│   │       └── ConfigManager.kt          # App configuration
│   ├── 📁 src/test/java/com/aigen/aicore/
│   │   ├── LLMManagerTest.kt
│   │   ├── MemoryManagerTest.kt
│   │   └── ToolExecutorTest.kt
│   └── build.gradle.kts                  # ai-core module build config
│
├── 📁 voice-processor/                    # Voice Processing Module
│   ├── 📁 src/main/java/com/aigen/voiceprocessor/
│   │   ├── 📁 speech/                    # Speech Processing
│   │   │   ├── SpeechEngines.kt          # STT & TTS interfaces
│   │   │   │   ├── SpeechToTextEngine    # STT interface
│   │   │   │   ├── TextToSpeechEngine    # TTS interface
│   │   │   │   ├── WhisperSTTEngine      # Whisper.cpp implementation
│   │   │   │   └── PiperTTSEngine        # Piper implementation
│   │   │   ├── AudioProcessor.kt         # Audio handling
│   │   │   └── VoiceRecorder.kt          # Audio recording
│   │   └── 📁 audio/                     # Audio utilities
│   ├── 📁 src/test/java/com/aigen/voiceprocessor/
│   └── build.gradle.kts                  # voice-processor module config
│
├── 📁 gradle/                             # Gradle wrapper
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── 📄 build.gradle.kts                   # Root build configuration
├── 📄 settings.gradle.kts                # Project settings
├── 📄 gradlew                            # Gradle wrapper (Unix)
├── 📄 gradlew.bat                        # Gradle wrapper (Windows)
│
├── 📄 README.md                          # Main documentation
├── 📄 QUICKSTART.md                      # Quick start guide
├── 📄 ARCHITECTURE.md                    # System architecture
├── 📄 IMPLEMENTATION_GUIDE.md            # Implementation details
├── 📄 ROADMAP.md                         # Development roadmap
├── 📄 CONTRIBUTING.md                    # Contribution guidelines
├── 📄 FAQ.md                             # Frequently asked questions
│
├── 📄 LICENSE                            # Apache 2.0 License
├── 📄 .gitignore                         # Git ignore rules
│
├── 🔧 setup.sh                           # Setup script
├── 🔧 download_model.sh                  # Model download helper
└── 🔧 setup-dev.sh                       # Dev environment setup
```

## Module Dependencies

```
app (Android App)
├── depends on: ai-core
├── depends on: voice-processor
└── depends on: Jetpack libraries

ai-core (Core Logic)
├── depends on: kotlin-stdlib
├── depends on: androidx.room:room-runtime
├── depends on: kotlinx-serialization
└── independent from Android UI

voice-processor (Voice Processing)
├── depends on: kotlin-stdlib
├── optional: Whisper.cpp JNI
├── optional: Piper TTS JNI
└── independent from AI core
```

## Key File Descriptions

### App Layer (UI & UX)
| File | Purpose | Lines |
|------|---------|-------|
| MainActivity.kt | App entry point, Compose setup | ~50 |
| ChatScreen.kt | Main chat interface | ~150 |
| ChatViewModel.kt | Chat logic, message handling | ~100 |
| Navigation.kt | Screen routing & drawer | ~100 |

### AI Core Layer (Business Logic)
| File | Purpose | Lines |
|------|---------|-------|
| LLMManager.kt | LLM coordination | ~120 |
| ToolExecutor.kt | Tool execution engine | ~200 |
| MemoryManager.kt | Memory storage & retrieval | ~80 |

### Data Layer (Persistence)
| File | Purpose | Lines |
|------|---------|-------|
| AppDatabase.kt | Room database setup | ~20 |
| Entities.kt | Database entities | ~60 |
| Daos.kt | Data access objects | ~120 |

### Voice Layer (Speech Processing)
| File | Purpose | Lines |
|------|---------|-------|
| SpeechEngines.kt | STT/TTS interfaces | ~100 |

---

## Important Patterns

### 1. **Module Independence**
- `ai-core` has NO Android dependencies
- Can be tested independently
- Can be used in other Android projects

### 2. **Interface-Based Design**
- `LLMEngine` interface allows multiple implementations
- `Tool` interface allows extensible tool system
- `SpeechToTextEngine` interface for different STT backends

### 3. **Flow-Based Streaming**
- Tokens stream via `Flow<String>`
- Allows reactive UI updates
- Supports cancellation

### 4. **MVVM Architecture**
- ViewModels manage state
- UI observes StateFlow
- Separation of concerns

---

## File Statistics

- **Total Kotlin Files**: ~40
- **Total Java Files**: 0 (Pure Kotlin)
- **Total Resource Files**: ~20
- **Lines of Code (Estimated)**: ~3000+
- **Test Coverage (Target)**: >80%
- **Documentation**: >500 lines

---

For more details on specific modules, see:
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design
- [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md) - Implementation details
- [README.md](README.md) - Feature overview
