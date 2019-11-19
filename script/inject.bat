adb push ./app/.externalNativeBuild/cmake/Spec_emuDebug/armeabi-v7a/javahook/inject /data/local/tmp
adb shell su -c "chmod 775 /data/local/tmp/inject"
adb shell su -c "/data/local/tmp/inject"