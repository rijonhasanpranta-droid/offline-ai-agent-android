# Contributing to Offline AI Agent

Thank you for your interest in contributing! This document provides guidelines and instructions.

## Code of Conduct

Be respectful, inclusive, and professional in all interactions.

## How to Contribute

### Reporting Bugs

1. Check if the bug already exists in issues
2. Use the bug report template
3. Include:
   - Device info (model, Android version)
   - Steps to reproduce
   - Expected vs actual behavior
   - Logs if available

### Suggesting Features

1. Check existing feature requests
2. Describe the use case clearly
3. Explain why it's important
4. Provide mockups if UI-related

### Submitting Code

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Follow Kotlin code style guidelines
4. Add tests for new functionality
5. Write clear commit messages
6. Push to your fork
7. Create a Pull Request with detailed description

## Code Style

- Follow [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable names
- Add comments for complex logic
- Format code: `./gradlew ktlintFormat`

## Testing

- Write unit tests for business logic
- Add integration tests for database operations
- Test on multiple API levels (26, 29, 31, 34)
- Test on different RAM configurations

## Documentation

- Update README for new features
- Add comments to public APIs
- Document configuration options
- Include usage examples

## Areas We Need Help

### High Priority
- [ ] Complete LLaMA.cpp JNI wrapper
- [ ] Implement streaming token rendering
- [ ] Add markdown rendering for chat

### Medium Priority
- [ ] Voice input/output implementation
- [ ] Performance optimization for large models
- [ ] Bangla language improvements

### Low Priority
- [ ] UI/UX refinements
- [ ] Documentation improvements
- [ ] Example models and guides

## Pull Request Process

1. Ensure code builds without warnings
2. All tests pass locally
3. Update documentation
4. Request review from maintainers
5. Address feedback
6. Merge after approval

## Development Setup

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/offline-ai-agent-android.git
cd offline-ai-agent-android

# Create feature branch
git checkout -b feature/amazing-feature

# Make changes...

# Build and test
./gradlew build
./gradlew connectedAndroidTest
```

## Questions?

Feel free to open a discussion or issue. Happy contributing! 🚀
