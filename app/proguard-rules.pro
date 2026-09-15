# Preserve classes and methods for reflection-based code
-keepclassmembers class * {
    *** *(...);
}

# Keep Timber logging
-dontwarn org.jetbrains.annotations.**

# Keep Kotlin metadata
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Keep Room database
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep ONNX Runtime
-keep class com.microsoft.onnxruntime.** { *; }
-keep interface com.microsoft.onnxruntime.** { *; }

# Keep serialization classes
-keepclassmembers class com.aigen.** {
    *** *(...); 
}
