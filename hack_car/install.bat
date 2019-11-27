echo @off
rem adb shell mkdir /data/local/tmp/anti_protect
rem adb shell rm -rf /data/local/tmp/anti_protect/*
rem adb push .\anti_protect\injectit /data/local/tmp/anti_protect/inject
rem adb push .\anti_protect\libHookUtil.so /data/local/tmp/anti_protect/libHookUtil.so
rem adb push .\anti_protect\libnewFunc.so /data/local/tmp/anti_protect/libanti_debug.so
rem adb push new_cmdline /data/local/tmp/anti_protect/new_cmdline
rem adb push new_maps /data/local/tmp/anti_protect/new_maps
rem adb push new_status /data/local/tmp/anti_protect/new_status
rem adb push new_tcp /data/local/tmp/anti_protect/new_tcp
rem adb push new_wchan /data/local/tmp/anti_protect/new_wchan
rem adb shell chmod 777 /data/local/tmp/anti_protect/injectit
rem adb shell ./data/local/tmp/anti_protect/injectit zygote /data/local/tmp/anti_protect/libanti_debug.so 1
rem set BuildTool=%AD_NDK%\build\ndk-build

rem call %BuildTool% NDK_PROJECT_PATH=./inject      SYSROOT=%AD_NDK%\sysroot   APP_BUILD_SCRIPT=inject/Android.mk      APP_ABI=%__APP_ABI%
rem call %BuildTool% NDK_PROJECT_PATH=./HookUtil    SYSROOT=%AD_NDK%\sysroot   APP_BUILD_SCRIPT=HookUtil/Android.mk    APP_ABI=%__APP_ABI%
rem call %BuildTool% NDK_PROJECT_PATH=./AAnti_debug SYSROOT=%AD_NDK%\sysroot   APP_BUILD_SCRIPT=AAnti_debug/Android.mk APP_ABI=%__APP_ABI%
rem prepare env stage one.
set __APP_ABI=armeabi-v7a
rem del /F /S /Q inject\libs
rem del /F /S /Q HookUtil\libs
del /F /S/ Q data
mkdir data
copy ..\app\build\intermediates\cmake\Spec_emu\debug\obj\%__APP_ABI%\libHookUtil.so         .\data\
copy ..\app\build\intermediates\cmake\Spec_emu\debug\obj\%__APP_ABI%\inject              .\data\
copy ..\app\build\intermediates\cmake\Spec_emu\debug\obj\%__APP_ABI%\libanti.so     .\data\






rem prepare env stage two.

adb push data/inject /data/local/tmp/inject
adb push data/libHookUtil.so /data/local/tmp/libtest.so
adb shell su -c "chmod 775 /data/local/tmp/inject"
adb shell su -c "chmod 775 /data/local/tmp/libtest.so"




