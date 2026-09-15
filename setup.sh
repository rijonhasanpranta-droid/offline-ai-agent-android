#!/bin/bash
# Quick start script for Offline AI Agent development

set -e

echo "🚀 Offline AI Agent - Setup Script"
echo "==================================="
echo ""

# Check if Android SDK is installed
if [ -z "$ANDROID_SDK_ROOT" ] && [ -z "$ANDROID_HOME" ]; then
    echo "❌ Error: Android SDK not found"
    echo "Please install Android SDK and set ANDROID_HOME environment variable"
    exit 1
fi

echo "✅ Android SDK found"

# Check if NDK is installed
if [ -z "$ANDROID_NDK_ROOT" ]; then
    echo "⚠️  Warning: Android NDK not found"
    echo "NDK is needed for native LLM libraries"
    echo "Install via: Android Studio > SDK Manager > SDK Tools > NDK"
else
    echo "✅ Android NDK found"
fi

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java not found"
    echo "Please install Java 17 or later"
    exit 1
fi

java_version=$(java -version 2>&1 | head -n 1)
echo "✅ Java found: $java_version"

# Check Gradle
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found"
    echo "Please run from project root directory"
    exit 1
fi

echo "✅ Gradle wrapper found"
echo ""

# Clean and build
echo "🔨 Building project..."
./gradlew clean build

echo ""
echo "✅ Setup complete!"
echo ""
echo "Next steps:"
echo "1. Open project in Android Studio"
echo "2. Create/select emulator or connect physical device"
echo "3. Download a quantized GGUF model from:"
echo "   - https://huggingface.co/TheBloke (recommended)"
echo "   - https://ollama.ai (easy setup)"
echo "4. Transfer model to device: adb push model.gguf /sdcard/Download/"
echo "5. Run: ./gradlew installDebug"
echo "6. Open app > Model Management > Load model"
echo ""
echo "Recommended first model: TinyLlama-1.1B (lightweight, good for testing)"
echo "Recommended powerful model: Mistral-7B (better quality, needs 6GB+ RAM)"
echo ""
echo "For help, check:"
echo "- README.md - Feature overview"
echo "- ARCHITECTURE.md - System design"
echo "- IMPLEMENTATION_GUIDE.md - Development guide"
echo "- FAQ.md - Common questions"
