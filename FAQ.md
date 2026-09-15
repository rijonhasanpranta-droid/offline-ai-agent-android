# Offline AI Agent - Frequently Asked Questions

## General

### Q: Is the app truly offline?
**A:** Yes. The core AI conversation works completely offline. No internet connection is required. Voice features (when implemented) will also be offline.

### Q: What happens when I lose internet?
**A:** Nothing changes - the app continues working exactly the same. Internet is optional for future features like cloud backup or model downloads.

### Q: How much storage do I need?
**A:** Depends on the model:
- Phi-2 (2.7B): ~2GB quantized
- TinyLlama (1.1B): ~1GB quantized  
- Mistral (7B): ~4GB quantized
- Plus app + conversation history (~100MB typical)

### Q: Can I use this without a phone with high specs?
**A:** Yes, but with limitations:
- 2GB RAM: Use TinyLlama (1.1B) or smaller
- 4GB RAM: Use Phi-2 (2.7B) or TinyLlama
- 6GB+ RAM: Use 7B models like Mistral

## Models & Performance

### Q: Which model should I use?
**A:** Recommended by device capacity:
- **Limited (2GB RAM)**: TinyLlama-1.1B
- **Standard (4GB RAM)**: Phi-2 (2.7B)
- **High-end (6GB+ RAM)**: Mistral-7B or OpenHermes-7B

### Q: Why is inference slow?
**A:** Mobile inference is inherently slower than desktop. Factors:
- Model size (7B models are slower)
- Quantization level (lower = faster but less accurate)
- Device CPU speed
- Available RAM

**Optimization tips:**
- Use Q4_K_M or lighter quantization
- Reduce context length
- Use smaller models
- Close other apps

### Q: How do I switch models?
**A:** Via Model Management screen:
1. Open settings
2. Go to Model Management
3. Unload current model
4. Enter path to new model
5. Load new model

### Q: Can I use GGUF models?
**A:** Yes, GGUF is the recommended format. Download from Hugging Face:
- TheBloke's quantized models
- ollama.ai model library

## Multilingual Support

### Q: Does it support Bangla?
**A:** Yes, for text. Models trained on Bangla data will respond in Bangla. Examples:
- "আমার নাম কি?" → "Your name is what you tell me."
- "আজকের তারিখ কি?" → Uses DateTime tool

### Q: Why is Bangla response sometimes incorrect?
**A:** Model quality depends on training data. Most models are English-heavy. Improvements:
- Use Bangla-optimized models when available
- Fine-tune on Bangla data
- Use mixed English-Bangla queries

### Q: Will voice support Bangla?
**A:** Eventually, if using models with Bangla STT/TTS support.

## Privacy

### Q: Where is my data stored?
**A:** Only on your phone:
- Conversations: `/data/data/com.aigen.offlineai/databases/`
- Models: `/sdcard/Android/data/com.aigen.offlineai/files/models/`
- Memories: Room database (device storage)
- Settings: SharedPreferences (device storage)

### Q: Can I export my conversations?
**A:** Not yet, but it's a planned feature. Currently:
- Conversations stored in local SQLite
- Manual backup via adb:
  ```bash
  adb backup -f backup.ab com.aigen.offlineai
  ```

### Q: Does the app track me?
**A:** No. The app has:
- No analytics
- No telemetry
- No tracking pixels
- No external requests
- No ads

### Q: Is my model secure?
**A:** Yes. Models stored locally with standard Android permissions. Only the app can access.

## Technical

### Q: How does token streaming work?
**A:** The LLM generates tokens one-by-one. Each token is displayed as it's generated:
```
Generated:
"The"
"The cat"
"The cat sat"
"The cat sat on"
...  (complete response appears incrementally)
```

### Q: Why did the app crash?
**A:** Common causes:
- **Out of memory**: Model too large for device
- **Storage full**: Not enough space for model
- **Corrupted model**: Download model again
- **Incompatible Android version**: Minimum is Android 8

### Q: How much RAM does inference use?
**A:** Approximately:
- TinyLlama (1.1B): ~600MB
- Phi-2 (2.7B): ~1.4GB
- Mistral (7B): ~3-4GB
- Plus OS + other apps

### Q: Can I run multiple models simultaneously?
**A:** No. Load one model at a time. To switch:
1. Unload current model (frees RAM)
2. Load new model

### Q: How is context managed?
**A:** Configurable in settings:
- Default: 2048 tokens of context
- Larger = more conversation history remembered
- Larger = more memory used
- Larger = slower inference

## Voice Features (When Implemented)

### Q: Will voice work completely offline?
**A:** Yes. Using:
- Whisper.cpp for speech-to-text (offline)
- Piper TTS for text-to-speech (offline)
- No cloud service dependency

### Q: What languages will voice support?
**A:** Initially English, Bangla support pending model availability.

### Q: How accurate is offline speech recognition?
**A:** Usually 85-95% depending on:
- Audio quality
- Accent/pronunciation
- Background noise
- Model version

## Troubleshooting

### Q: Model fails to load
**Solutions:**
- Check file path is correct
- Verify model file exists and isn't corrupted
- Check available RAM: `Settings > Device info > Memory`
- Try smaller quantization
- Restart app and device

### Q: Inference is very slow
**Solutions:**
- Close other apps
- Reduce max tokens in settings
- Use smaller model
- Reduce context length
- Restart device

### Q: Bangla text not displaying correctly
**Solutions:**
- Update Android to latest version
- Clear app cache: `Settings > Apps > Offline AI Agent > Storage > Clear Cache`
- Reinstall app
- Check if font supports Bangla

### Q: Database error / conversations lost
**Solutions:**
- App uses Room database (SQLite)
- Database file: `/data/data/com.aigen.offlineai/databases/offline_ai_db`
- Backup via adb if possible
- Clear app data only as last resort

### Q: Can't find model file path
**Solution:** Use File Manager app:
1. Open your file manager
2. Navigate to where you placed model.gguf
3. Long press → Properties to see full path
4. Copy path to app

## Features & Limitations

### Q: Can it search the web?
**A:** No, it's offline. It can only work with:
- Built-in tools (calculator, date/time)
- Local files (if File Search tool is implemented)
- Memory of previous conversations

### Q: Can it use my documents/files?
**A:** Partially. Planned File Search tool will allow:
- Searching file names
- Reading file contents (with permissions)
- Not full document understanding yet

### Q: Can it see my photos/videos?
**A:** No, unless you explicitly share via clipboard or file selection.

### Q: Will it get updates?
**A:** Yes. Check GitHub for updates. Improvements include:
- Better model support
- Performance optimization
- Bug fixes
- New features

## Development

### Q: Can I contribute?
**A:** Yes! See [CONTRIBUTING.md](CONTRIBUTING.md). Help needed:
- LLM integration
- Voice features
- UI improvements
- Bug fixes
- Documentation

### Q: Can I fork and customize?
**A:** Yes, it's open source (Apache 2.0). You can:
- Customize UI
- Add features
- Use as library
- Create variants

### Q: How do I report bugs?
**A:** Open an issue on GitHub with:
- Device info
- Android version
- Exact steps to reproduce
- Error logs
- Screenshots

---

**Still have questions?** Open an issue or discussion on GitHub!
