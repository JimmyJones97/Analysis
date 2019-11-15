adb push ./app/.externalNativeBuild/cmake/Spec_emuDebug/armeabi-v7a/javahook/asm_ex /data/local/tmp
adb shell su -c "chmod 775 /data/local/tmp/asm_ex"
adb shell su -c "/data/local/tmp/asm_ex"