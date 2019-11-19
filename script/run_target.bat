adb push ./app/.externalNativeBuild/cmake/Spec_emuDebug/armeabi-v7a/helloworld/helloworld /data/local/tmp
adb shell su -c "chmod 775 /data/local/tmp/helloworld"
adb shell su -c "/data/local/tmp/helloworld"