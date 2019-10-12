package com.example.pwd61.analysis.Detour.fuckApp;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:eleme
 * Created by pwd61 on 10/12/2019 4:14 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class eleme {
    static private String TAG = "ELE";
    static boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {
        /**
         * 挂钩在java.lang.Runtime中loadLibrary（）。
         有专门为检查根目录的库。 这有助于我们阻止
         */
        findAndHookMethod("java.lang.Runtime", lpparam.classLoader, "loadLibrary", String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String libname = (String) param.args[0];
                Log.d(TAG, "Loading of library--> lib" + libname + ".so after.");
                if (libname.equals("tnet-3.1.14") && isFirstLoad) {
                    Log.d(TAG, "loading our library");
                    System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libnative_lib.so");
                    isFirstLoad = false;
                }
            }
        });
    }
}
