@echo off
rem ../app/src/main/cpp/out/library/libpltfun.so
rem
adb push ../app/src/main/cpp/out/library/libpltfun.so /data/local/tmp
adb push ../app/src/main/cpp/out/build/plt_hook_target /data/local/tmp/main
adb shell "chmod 775 /data/local/tmp/main"
adb shell "export LD_LIBRARY_PATH=/data/local/tmp; /data/local/tmp/main"