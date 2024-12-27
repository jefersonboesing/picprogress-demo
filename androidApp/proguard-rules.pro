-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

-if @kotlinx.serialization.Serializable class **
-keep class <1> { *; }

-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.picprogress.**$$serializer { *; }
-keepclassmembers class com.picprogress.** {
    *** Companion;
}
-keepclasseswithmembers class com.picprogress.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep class com.picprogress.local.** { *; }
-keep class com.picprogress.model.** { *; }
-keep class compicprogress.** { *; }