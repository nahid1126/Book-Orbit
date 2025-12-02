
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**

-dontwarn org.apache.**

-dontwarn java.lang.management.**
-dontwarn javax.lang.model.**
-dontwarn java.sql.JDBCType

# Keep Firebase & Firestore Models
-keep class com.nahid.book_orbit.data.remote.dto.** { *; }
-keep class com.nahid.book_orbit.data.local.entity.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

-keep class com.nahid.book_orbit.data.remote.dto.** { <fields>; }
-keep class com.nahid.book_orbit.data.local.entity.** { <fields>; }




# Keep Google BillingClient classes
-keep class com.android.billingclient.** { *; }

# Keep PurchasesUpdatedListener lambdas
-keepclassmembers class * implements com.android.billingclient.api.PurchasesUpdatedListener {
    public void onPurchasesUpdated(...);
}


 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep class com.android.org.conscrypt.** { *; }
-keep class javax.annotation.** { *; }

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**
# Preserve Ktor core features
-keep class io.ktor.** { *; }

# Required to avoid stripping coroutine metadata
-keepclassmembers class kotlinx.coroutines.** {
    *;
}

# Keep kotlinx.serialization if used
-keep class kotlinx.serialization.** { *; }

-keep class * {
 @kotlinx.serialization.SerialName <fields>;
}

# Needed if you're using Ktor Client with Serialization
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

# Ktor uses reflection on some internal engine classes
-keep class io.ktor.client.engine.** { *; }
-keep class io.ktor.util.** { *; }

# Ktor Logging (optional, if you're using logging features)
-keep class io.ktor.client.plugins.logging.** { *; }

# Prevent R8/ProGuard from removing default constructors (useful for engines and interceptors)
-keepclassmembers class * {
    public <init>(...);
}


##---------------End: proguard configuration for Gson  ----------