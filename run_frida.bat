adb push ./frida-server-12.6.11-android-arm64 /data/local/tmp/frida-server
adb shell su root  "chmod 775 /data/local/tmp/frida-server"
adb shell su root "/data/local/tmp/frida-server "

REM jnitrace -l libjdbitmapkit.so -b accurate -d -p com.example.pwd66.fuckjd
