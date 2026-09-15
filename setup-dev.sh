# Local development setup
echo "Setting up local development environment..."

# Create directories
mkdir -p app/src/main/res/drawable
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/values
mkdir -p app/src/test/java/com/aigen/offlineai
mkdir -p app/src/androidTest/java/com/aigen/offlineai

# Create empty test files for structure
echo "// Empty test file" > app/src/test/java/com/aigen/offlineai/ChatViewModelTest.kt
echo "// Empty test file" > app/src/androidTest/java/com/aigen/offlineai/ChatScreenTest.kt

echo "✅ Development environment ready!"
