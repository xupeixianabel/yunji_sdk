# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
#混淆采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontshrink
-dontoptimize
-useuniqueclassmembernames
-keepattributes Exceptions,InnerClass
-keep class com.yunji.handler.YunJiApiFactory{*;}
-keep class com.yunji.handler.api.IYunJiAPIManger{*;}
-keep class com.yunji.handler.api.ICallback{*;}
-keep class com.yunji.handler.api.IResultCallback{*;}
-keep class com.yunji.handler.log.**{*;} #SDK给第三方客户使用时，-keep class com.yunji.handler.log.**{*;}可以注释掉
-keep class com.yunji.handler.vo.** {
    <fields>;
    <methods>;
}