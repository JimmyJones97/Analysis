package com.example.pwd61.analysis.Detour.fuck;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Zhaoshang
 * Created by pwd61 on 2019/5/7 15:15
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class Zhaoshang {
    static private String TAG = "zhaoshang";
    static private boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {
        /**
         * 挂钩在java.lang.Runtime中loadLibrary（）。
         * 有专门为检查根目录的库。 这有助于我们阻止
         */
        findAndHookMethod("java.lang.Runtime", lpparam.classLoader, "loadLibrary", String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String libname = (String) param.args[0];
                Log.d(TAG, "Loading of library--> lib" + libname + ".so after.");
                if (libname.equalsIgnoreCase("CmbShield") && isFirstLoad) {
                    Log.d(TAG, "Start to load cmb_bank.so");
                    System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libcmb_bank.so");
                    //System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libilongyuan.so");
                    isFirstLoad = false;
                }
            }
        });
    }
}
