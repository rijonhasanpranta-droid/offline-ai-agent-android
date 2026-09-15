# Offline AI Agent Android - Getting Started Guide

## 📱 Quick Start (5 minutes)

### 1. **Clone & Build**
```bash
git clone https://github.com/rijonhasanpranta-droid/offline-ai-agent-android.git
cd offline-ai-agent-android
./gradlew build
```

### 2. **Get a Model**

Download a quantized GGUF model (recommended from HuggingFace):

**Option A: TinyLlama (Recommended for first test - 1.1B)**
```bash
# Size: ~650MB | RAM: 1-2GB | Speed: Fast
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
adb push tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf /sdcard/Download/
```

**Option B: Phi-2 (Balanced - 2.7B)**
```bash
# Size: ~1.5GB | RAM: 2-3GB | Speed: Good
wget https://huggingface.co/TheBloke/phi-2-GGUF/resolve/main/phi-2.Q4_K_M.gguf
adb push phi-2.Q4_K_M.gguf /sdcard/Download/
```

**Option C: Mistral-7B (Powerful - 7B)**
```bash
# Size: ~4GB | RAM: 4-6GB | Speed: Slower but better quality
wget https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.1-GGUF/resolve/main/mistral-7b-instruct-v0.1.Q4_K_M.gguf
adb push mistral-7b-instruct-v0.1.Q4_K_M.gguf /sdcard/Download/
```

### 3. **Run on Device**
```bash
./gradlew installDebug
# Or use: adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 4. **Load Model in App**
1. Open **Offline AI Agent**
2. Open menu (☰) → **Models**
3. Enter model path: `/storage/emulated/0/Download/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf`
4. Tap **Load Model**
5. Wait for loading... ⏳
6. Once loaded, go back to **Chat** and start typing! 🚀

---

## 🎯 Core Features at a Glance

### ✅ Works Completely Offline
- No internet required
- No cloud API dependency
- All processing happens on your device
- No data leaves your phone

### ✅ Beautiful Chat Interface
```
User:    "What is 25 × 48?"
AI:      "Let me calculate that. 25 × 48 = 1200"

User:    "আজকের তারিখ কি?" (What's today's date?)
AI:      "Today is December 15, 2024"
```

### ✅ Smart Agent System
- **Calculator Tool** - Math expressions
- **DateTime Tool** - Current date/time
- **Clipboard Tool** - Read/write clipboard
- **Device Info** - RAM, storage, device info
- **Notes Tool** - Create local notes
- More tools coming soon!

### ✅ Persistent Conversations
- Auto-save chat history
- Search past conversations
- Rename & organize chats
- Pin favorite conversations

### ✅ Local Memory System
- Remember facts about you
- Personalized responses
- Category-based storage
- Full privacy - never leaves your phone

### ✅ Multilingual Support
- English ✓
- Bangla (বাংলা) ✓
- Mix Bangla-English freely

---

## 🔧 System Requirements

### Minimum
- **Android:** 8.0+ (API 26)
- **RAM:** 2GB
- **Storage:** 1GB free
- **Processor:** Any modern ARM processor

### Recommended (for 7B models)
- **Android:** 10.0+ (API 29)
- **RAM:** 6GB+
- **Storage:** 2GB free
- **Processor:** Snapdragon 855+ or equivalent

### Device Examples

| Device Type | Recommended Model | Notes |
|---|---|---|
| Budget (2GB RAM) | TinyLlama (1.1B) | Lightweight, testing only |
| Mid-range (4GB RAM) | Phi-2 (2.7B) | Good balance of quality & speed |
| High-end (6GB+ RAM) | Mistral-7B | Best quality, slower |
| Tablet (8GB+ RAM) | Mistral-7B or better | Can handle larger models |

---

## 📖 Documentation

### For Users
- **[README.md](README.md)** - Full feature overview
- **[FAQ.md](FAQ.md)** - Answers to common questions
- **[ROADMAP.md](ROADMAP.md)** - What's coming next

### For Developers  
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System design & data flow
- **[IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md)** - How to implement features
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - How to contribute code

---

## 🎮 Using the App

### Chat Screen
```
┌─────────────────────────────────┐
│  Offline AI Agent      [☰]      │  ← Menu button
├─────────────────────────────────┤
│                                 │
│  AI:  Hello! How can I help?    │  ← AI messages
│  [Copy] [✎] [🗑]               │  ← Actions
│                                 │
│                            [👤] │  
│                   You: Hi there  │  ← Your messages
│  [✎] [🗑]                       │
│                                 │
├─────────────────────────────────┤
│  [Type message...]  [🎤] [➤]    │  ← Input area
└─────────────────────────────────┘
```

### Menu Options
- **💬 Chat** - Main conversation
- **📚 Conversations** - View & manage chat history
- **🧠 Memory** - View & manage memories
- **🤖 Models** - Load/unload LLM models
- **⚙️ Settings** - Adjust temperature, tokens, etc.

---

## ⚡ Tips & Tricks

### Getting Better Responses
```
✅ Good:  "Explain quantum computing"
❌ Bad:   "quantum"

✅ Good:  "Calculate 2^10 using the calculator"
❌ Bad:   "how much is 2^10"

✅ Good:  "What time is it?"
❌ Bad:   "time"
```

### Performance Tips
1. **Close other apps** - Frees up RAM for inference
2. **Reduce max tokens** - Faster responses, less text
3. **Lower temperature** - More focused responses
4. **Restart device** - Clears memory caches
5. **Use smaller model** - If too slow, try TinyLlama

### Bangla Usage
```
User:  "আমার নাম করিম। আমি ঢাকায় থাকি।"
AI:    "আপনার নাম করিম এবং আপনি ঢাকায় থাকেন।"

User:  "আমার favorite color কী?"
AI:    "আপনি আপনার প্রিয় রঙ বলেননি।"
```

---

## 🔴 Troubleshooting

### Problem: App crashes on startup
**Solution:**
1. Clear app data: Settings → Apps → Offline AI Agent → Storage → Clear Data
2. Reinstall app: `./gradlew installDebug`
3. Check Android version (minimum 8.0)

### Problem: Model fails to load
**Solution:**
1. Check file path is correct
2. Verify file exists: `adb shell ls -la /sdcard/Download/`
3. Ensure 2GB+ free RAM
4. Try smaller model (TinyLlama)
5. Check available space: `adb shell df /storage/emulated/0/`

### Problem: Inference is very slow
**Solution:**
1. Close other apps (free RAM)
2. Reduce max tokens in settings
3. Lower temperature
4. Use quantization (Q4_K_M recommended)
5. Try smaller model
6. Restart device

### Problem: Bangla text shows as boxes (□□□)
**Solution:**
1. Update Android to latest version
2. Update system fonts
3. Try different font in system settings
4. Clear app cache: `adb shell pm clear com.aigen.offlineai`

### Problem: Conversations not saving
**Solution:**
1. Check storage space available
2. Verify app has storage permission
3. Restart app
4. Reinstall if issue persists

---

## 📊 Device Compatibility

### ✅ Tested Working
- Samsung Galaxy S20 and newer
- Google Pixel 4a and newer
- OnePlus 8 and newer
- Xiaomi Poco X3 and newer
- Any Android 10+ device with 4GB+ RAM

### ⚠️ May Have Issues
- Devices with <2GB RAM
- Android versions < 8.0
- Very old processors (pre-2017)
- Limited storage (<500MB free)

### ❌ Not Compatible
- iOS (architecture limitation)
- Chromebook (requires full Android support)
- Smartwatches (too small/limited)

---

## 🚀 Next Steps

### For Users
1. ✅ Install app
2. ✅ Download a model
3. ✅ Load model in app
4. ✅ Start chatting
5. 📖 Read [FAQ.md](FAQ.md) for more info
6. 💬 Join discussions on GitHub

### For Developers
1. ✅ Clone repository
2. ✅ Read [ARCHITECTURE.md](ARCHITECTURE.md)
3. ✅ Check [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md)
4. ✅ Look at [CONTRIBUTING.md](CONTRIBUTING.md)
5. 🔧 Set up development environment
6. 🎯 Pick a feature from [ROADMAP.md](ROADMAP.md)
7. 🚀 Submit PR!

---

## 💬 Community

- **GitHub Issues** - Bug reports & feature requests
- **GitHub Discussions** - Questions & ideas
- **Pull Requests** - Code contributions welcome!
- **Feedback** - Share your experience

---

## 📄 License

Apache License 2.0 - See [LICENSE](LICENSE) for details.

---

## 🙏 Credits

- [LLaMA.cpp](https://github.com/ggerganov/llama.cpp) - Efficient LLM inference
- [ONNX Runtime](https://onnxruntime.ai/) - Multi-framework inference
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI
- [Room Database](https://developer.android.com/training/data-storage/room) - Local persistence
- [TheBloke](https://huggingface.co/TheBloke) - Quantized models

---

**Enjoy your fully offline AI assistant! 🚀**

*Made with ❤️ for privacy-first AI on mobile*
