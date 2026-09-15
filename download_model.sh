#!/bin/bash
# Script to download and setup a recommended model

set -e

echo "📦 Model Setup for Offline AI Agent"
echo "===================================="
echo ""

if [ $# -eq 0 ]; then
    echo "Available models:"
    echo ""
    echo "1. tinyllama (1.1B - RECOMMENDED for testing)"
    echo "   Size: ~650MB | RAM needed: 1-2GB"
    echo "   Good for: Testing, low-end devices"
    echo ""
    echo "2. phi (2.7B - BALANCED)"
    echo "   Size: ~1.5GB | RAM needed: 2-3GB"
    echo "   Good for: Mid-range devices, good quality"
    echo ""
    echo "3. mistral (7B - POWERFUL)"
    echo "   Size: ~4GB | RAM needed: 4-6GB"
    echo "   Good for: High-end devices, best quality"
    echo ""
    echo "Usage: ./download_model.sh [model_name]"
    echo "Example: ./download_model.sh tinyllama"
    exit 0
fi

MODEL_NAME=$1
DOWNLOAD_DIR="./models"

mkdir -p "$DOWNLOAD_DIR"

echo "Downloading $MODEL_NAME model..."
echo ""

case $MODEL_NAME in
    tinyllama)
        URL="https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf"
        FILE="tinyllama-1.1b.gguf"
        ;;
    phi)
        URL="https://huggingface.co/TheBloke/phi-2-GGUF/resolve/main/phi-2.Q4_K_M.gguf"
        FILE="phi-2.gguf"
        ;;
    mistral)
        URL="https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.1-GGUF/resolve/main/mistral-7b-instruct-v0.1.Q4_K_M.gguf"
        FILE="mistral-7b.gguf"
        ;;
    *)
        echo "❌ Unknown model: $MODEL_NAME"
        echo "Available: tinyllama, phi, mistral"
        exit 1
        ;;
esac

if command -v wget &> /dev/null; then
    wget -O "$DOWNLOAD_DIR/$FILE" "$URL"
elif command -v curl &> /dev/null; then
    curl -L -o "$DOWNLOAD_DIR/$FILE" "$URL"
else
    echo "❌ Neither wget nor curl found"
    echo "Please download manually from: $URL"
    exit 1
fi

echo ""
echo "✅ Model downloaded to: $DOWNLOAD_DIR/$FILE"
echo ""
echo "Next steps:"
echo "1. Push to device: adb push $DOWNLOAD_DIR/$FILE /sdcard/Download/"
echo "2. In app, go to Model Management"
echo "3. Enter path: /storage/emulated/0/Download/$FILE"
echo "4. Click 'Load Model'"
echo ""
echo "Model ready! 🚀"
