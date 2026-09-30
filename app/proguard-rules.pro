# TensorFlow Lite: JNI가 이름으로 찾는 클래스가 있어 난독화·제거하면 추론이 깨진다.
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**
