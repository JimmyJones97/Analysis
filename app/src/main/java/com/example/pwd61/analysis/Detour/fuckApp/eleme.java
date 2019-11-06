package com.example.pwd61.analysis.Detour.fuckApp;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.print.PageRange;
import android.util.Log;

import com.android.tencent.qq.qq.Utils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.pwd61.analysis.Utils.utils.dumpStack;
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
    static private String TAG = "HACK-ELE";
    static private String STAG = "LOOKS";
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
                if (libname.contains("sg") && isFirstLoad) {
                    Log.d(TAG, "loading our library");
                    dumpStack();
                    //System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libnative_lib.so");
                    isFirstLoad = true;
                }
                if (libname.contains("erisxxxx") && isFirstLoad) {
//                    Log.d(TAG, "loading our library");
                    dumpStack();
                    //System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libnative_lib.so");
                    isFirstLoad = true;
                }
            }
        });
        findAndHookMethod("me.ele.android.enet.b.f", lpparam.classLoader, "b", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String res = (String) param.getResult();
                        Log.d(TAG, "XDEVICE_INFO->:" + res);
                    }
                }
        );
        findAndHookMethod("me.ele.android.enet.b.f", lpparam.classLoader, "a", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String res = (String) param.getResult();
                        Log.d(TAG, "memory->:" + res);
                    }
                }
        );
        findAndHookMethod("me.ele.uis.eris.ErisEntry", lpparam.classLoader, "sneer",
                Context.class,
                String.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String[] res = (String[]) param.getResult();
                        for (int i=0;i<res.length;i++) {
                            Log.d(STAG, "sneer["+i+"]->:"+res[i]);
                        }
                        Class ctx=(Class)param.args[0].getClass();
                        Log.d(STAG, "ErisEntry->:" +ctx.getName()+","+ param.args[1]+","+param.args[2]+","+param.args[3]);
                    }
                }
        );
    }
}
