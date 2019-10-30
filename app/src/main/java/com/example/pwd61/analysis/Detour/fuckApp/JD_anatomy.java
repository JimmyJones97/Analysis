package com.example.pwd61.analysis.Detour.fuckApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.example.pwd61.analysis.Detour.verfiy.tester;
import com.example.pwd61.analysis.Utils.ConvJSON;
import com.example.pwd61.analysis.Utils.FileUtils;
import com.example.pwd61.analysis.Utils.utils;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.pwd61.analysis.Utils.utils.dumpStack;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class JD_anatomy {

    static private String TAG = "HACK";
    static private boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {

//        findAndHookMethod("com.getkeepsafe.relinker.a.f", lpparam.classLoader,
//                "bX",
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        Log.d(TAG, "relinker lib");
//
//                    }
//                });
//        final Class<?> elfpare =
//                XposedHelpers.findClass("com.getkeepsafe.relinker.a.f", lpparam.classLoader);
//        findAndHookConstructor(elfpare, File.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
//                        File fils = (File) param.args[0];
//                        Log.d(TAG, "relinker file: " + fils.getName());
//                    }
//                }
//        );
//        findAndHookMethod("com.getkeepsafe.relinker.a.f", lpparam.classLoader,
//                "f",
//                File.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//
//                        File fils=(File)param.args[0];
//                        Log.d(TAG, "relinker file: "+fils.getName());
//
//                    }
//                });

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


        findAndHookMethod("jd.wjlogin_sdk.util.ab", lpparam.classLoader,
                "b",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        dumpStack();
                        utils.Log("UserInfoStoreUtil: toString:  str1:" + (String) param.args[0] + "=>" + (String) param.args[1]);
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.util.ab", lpparam.classLoader,
                "a",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        String res = (String) param.getResult();
//                        dumpStack();
                        utils.Log("UserInfoStoreUtil: toString:  str1:"   + (String) param.args[0] + "=>" + (String) param.args[1] + res);
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.a.e", lpparam.classLoader,
                "b",

                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        byte[] res = ( byte[]) param.getResult();

//                        dumpStack();
                        utils.Log("getkety: toString:  str1:" + new String(res));
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.a.b", lpparam.classLoader,
                "b",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        String res = ( String) param.getResult();

//                        dumpStack();
                        utils.Log("usrInfop: toString:  str1:"   + (String) param.args[0] + "=>" + (String) param.args[1] + res);
                    }
                });

    }

    static {
        //動態加載
        utils.Log("LOAD ourself so library!");
        //System.loadLibrary("native-lib");
        System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libnative_lib.so");
    }
}
