# ProGuard rules

# Keep all Android components and framework classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.fragment.app.Fragment

# Keep all model classes
-keep class com.lustia.story.data.** { *; }
-keep class com.lustia.story.engine.** { *; }

# Keep Gson serializable classes
-keepclassmembers class * {
    *** *;
}

# Optimize
-optimizationpasses 5
-verbose
