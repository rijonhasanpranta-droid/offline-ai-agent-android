# Offline AI Agent - Android

A modern, fully offline AI Agent mobile app that runs directly on your Android phone without requiring internet connection.

## 🎯 Core Features

### Offline AI Assistant
- **On-Device LLM**: Runs a quantized language model locally using LLaMA.cpp or ONNX Runtime
- **No Internet Required**: Complete AI conversations work without any network connectivity
- **Streaming Responses**: Real-time token generation for responsive UX
- **Adjustable Parameters**: Temperature, max tokens, context length configuration

### Smart Chat Interface
- **Beautiful Modern UI**: Built with Jetpack Compose for a premium feel
- **Multilingual Support**: Native support for English and Bangla (বাংলা)
- **Rich Message Features**:
  - User message editing
  - Message deletion
  - Response copying
  - Markdown and code block rendering
  - Message timestamps
  - Streaming indicator

### Conversation Management
- **Persistent Storage**: SQLite database for conversation history
- **Auto-Titling**: Automatically generates conversation titles
- **Conversation Drawer**: Search, rename, pin, and delete conversations
- **Clear Conversations**: Start fresh anytime

### AI Agent Architecture
- **Tool System**: Modular tools that the AI can use intelligently
- **Available Tools**:
  - **Calculator**: Mathematical expression evaluation
  - **Date/Time**: Current date and time retrieval
  - **Clipboard**: Read/write clipboard access
  - **Device Info**: RAM, storage, device information
  - **Notes**: Local note creation and management
  - **File Search**: Search local device files

### Voice Interaction (Planned)
- **Speech-to-Text**: Offline speech recognition using Whisper.cpp compatible implementation
- **Text-to-Speech**: Offline speech synthesis using Piper TTS
- **Push-to-Talk**: Natural voice conversation mode
- **Multilingual Voice**: Support for English and Bangla

### Local Memory System
- **Persistent Memory**: Remember user preferences and facts between sessions
- **Categorized Storage**: Organize memories by category
- **Memory Management**: View, edit, delete, or clear memories
- **Privacy First**: All memories stored locally, never sent to servers

### Model Management
- **Multiple Engine Support**:
  - LLaMA.cpp (primary)
  - ONNX Runtime
  - MLC LLM (future)
  - MediaPipe (future)
- **Model Loading Status**: Visual feedback during model initialization
- **RAM Management**: Graceful handling of insufficient memory
- **Configurable Models**: Easy to switch between different models

### Settings & Configuration
- **Temperature Control**: Adjust randomness (0.0 - 2.0)
- **Token Control**: Set maximum response length
- **Feature Toggles**: Enable/disable memory, voice, auto-save
- **Persistent Configuration**: Settings saved across sessions

## 📱 Architecture

### Project Structure
```
offline-ai-agent-android/
├── app/                          # Main Android app module
│   ├── src/main/java/com/aigen/offlineai/
│   │   ├── MainActivity.kt       # App entry point
│   │   ├── ui/
│   │   │   ├── screens/          # Compose screens
│   │   │   ├── components/       # Reusable UI components
│   │   │   └── theme/            # Material Design theme
│   │   └── viewmodel/            # MVVM ViewModels
│   └── build.gradle.kts
│
├── ai-core/                      # AI & LLM module
│   └── src/main/java/com/aigen/aicore/
│       ├── llm/                  # LLM engines & management
│       ├── agent/                # Tool system & execution
│       ├── memory/               # Memory management
│       ├── database/             # Room database
│       ├── model/                # Data models
│       └── config/               # Configuration
│
└── voice-processor/              # Speech processing module
    └── src/main/java/com/aigen/voiceprocessor/
        └── speech/               # STT & TTS engines
```

### Modular Design
- **ai-core**: Independent LLM inference and agent logic
- **voice-processor**: Isolated speech processing
- **app**: UI layer using Jetpack Compose
- Easy to test and extend each module independently

## 🛠️ Technology Stack

### Core
- **Language**: Kotlin
- **Minimum SDK**: Android 8 (API 26)
- **Target SDK**: Android 14 (API 34)
- **JVM Target**: 17

### UI
- **Jetpack Compose**: Modern declarative UI
- **Material Design 3**: Premium look and feel
- **Navigation**: Jetpack Navigation Compose
- **Accompanist**: Permission handling

### AI/ML
- **LLaMA.cpp JNI Bindings**: Local LLM inference
- **ONNX Runtime**: Alternative inference engine
- **Whisper.cpp**: Speech-to-text (planned)
- **Piper TTS**: Text-to-speech (planned)

### Data
- **Room Database**: Local persistent storage
- **Kotlin Coroutines**: Async operations
- **Kotlin Serialization**: JSON serialization
- **Timber**: Logging

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android NDK (for native LLM libraries)
- At least 4GB RAM on development machine

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/rijonhasanpranta-droid/offline-ai-agent-android.git
   cd offline-ai-agent-android
   ```

2. **Install Android NDK**
   - In Android Studio: Tools → SDK Manager → SDK Tools
   - Select "NDK (Side by side)" and install

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on emulator or device**
   ```bash
   ./gradlew installDebug
   ```

### Loading a Model

1. Download a quantized GGUF model (recommended for phones):
   - [Phi-2 (2.7B)](https://huggingface.co/TheBloke/phi-2-GGUF)
   - [TinyLlama (1.1B)](https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF)
   - [Mistral (7B)](https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.1-GGUF)

2. Transfer to your device:
   ```bash
   adb push model.gguf /sdcard/Download/
   ```

3. Open app → Model Management → Enter path → Load Model

## 📋 Feature Roadmap

### ✅ Completed
- [x] Project structure and Gradle setup
- [x] MVVM architecture with ViewModels
- [x] Room database integration
- [x] LLM engine interfaces (LLaMA.cpp, ONNX)
- [x] Tool system with modular design
- [x] Chat UI with Compose
- [x] Conversation management
- [x] Memory system
- [x] Configuration management
- [x] Settings screen

### 🔄 In Progress
- [ ] Complete LLaMA.cpp JNI integration
- [ ] Streaming token rendering
- [ ] Voice input/output
- [ ] Markdown rendering in chat
- [ ] Database persistence implementation

### 📅 Planned
- [ ] Vision support (image understanding)
- [ ] RAG (Retrieval Augmented Generation)
- [ ] Custom model fine-tuning on device
- [ ] Multi-language improvements
- [ ] Cloud sync (optional, with privacy preservation)
- [ ] Plugin system for custom tools
- [ ] Web UI companion app

## 📖 Usage Examples

### Text Conversation
```
User: "তোমার নাম কি?"
AI: "আমি একটি অফলাইন এআই সহায়ক। আমাকে যেকোনো প্রশ্ন করতে পারেন।"

User: "25 × 48 কত?"
AI: (Uses Calculator Tool) "The answer is 1200"
```

### Using Tools
```
User: "আজকের তারিখ কি?"
AI: (Uses DateTime Tool) "Today is December 15, 2024"

User: "একটি note save করো: কাল সকাল ৮টায় মিটিং"
AI: (Uses Notes Tool) "Note saved successfully!"
```

## 🔒 Privacy & Security

- ✅ **Zero Cloud Dependency**: Everything runs locally
- ✅ **No Data Exfiltration**: No user data leaves the device
- ✅ **No Tracking**: No telemetry or analytics
- ✅ **Open Source**: Fully transparent, auditable code
- ✅ **Local Storage**: All data in device's app directory

## ⚙️ System Requirements

### Minimum
- Android 8.0+ (API 26)
- 2GB RAM (4GB+ recommended for 7B models)
- 1GB free storage (for model + app)
- Offline capability

### Recommended for 7B Models
- Android 10.0+ (API 29)
- 6GB+ RAM
- 2GB free storage
- High-end processor (Snapdragon 888+)

## 🤝 Contributing

Contributions are welcome! Areas we need help with:

- [ ] LLaMA.cpp JNI wrapper completion
- [ ] Voice processing implementation
- [ ] UI/UX improvements
- [ ] Performance optimization
- [ ] Bangla language improvements
- [ ] Testing and bug fixes

Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📄 License

Apache License 2.0 - See [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [LLaMA.cpp](https://github.com/ggerganov/llama.cpp) - Efficient LLM inference
- [ONNX Runtime](https://onnxruntime.ai/) - Multi-framework inference
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI
- [Room Database](https://developer.android.com/training/data-storage/room) - Local persistence
- [Whisper.cpp](https://github.com/ggerganov/whisper.cpp) - Speech recognition
- [Piper TTS](https://github.com/rhasspy/piper) - Text-to-speech

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on [GitHub](https://github.com/rijonhasanpranta-droid/offline-ai-agent-android/issues)
- Start a discussion
- Check existing documentation

## 🎓 Learning Resources

- [Android Development Docs](https://developer.android.com/docs)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose/documentation)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Room Database Tutorial](https://developer.android.com/training/data-storage/room)
- [LLaMA.cpp Documentation](https://github.com/ggerganov/llama.cpp)

---

**Made with ❤️ for offline AI on mobile**
