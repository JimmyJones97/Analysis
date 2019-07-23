package com.example.pwd61.analysis.Detour.fuckApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


import com.example.pwd61.analysis.Utils.FileUtils;
import com.example.pwd61.analysis.app.yeecall.HexUtils;

import org.apache.commons.codec.binary.Hex;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.pwd61.analysis.Utils.Utils.dumpStack;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setIntField;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:yeecall
 * Created by pwd61 on 2019/7/9 14:56
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class yeecall {

    static private String TAG = "YEE";
    static boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("com.yeecall.app.hel", lpparam.classLoader,
                "a",
                Context.class,
                String.class,
                char[].class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        char[] bts = (char[]) param.getResult();
                        String sss = new String(bts);
                        Log.d(TAG, "CipherProtocol [:  " + param.args[1] + ",ret :" + sss);
                    }

                });

        findAndHookConstructor("com.yeecall.app.heq", lpparam.classLoader,
                Context.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "SecureDBHelper   table:" + param.args[1]);
                    }
                });
        findAndHookConstructor("com.yeecall.app.heq", lpparam.classLoader,
                Context.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "SecureDBHelper   table:" + param.args[1]);
                    }
                });
        findAndHookConstructor("com.yeecall.app.hep", lpparam.classLoader,
                "com.yeecall.app.heq",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "KeyValueDao constructor:" + param.args[1] + ",args2: " + param.args[2]);
                    }

                });
        findAndHookConstructor("com.yeecall.app.hen", lpparam.classLoader,
                Context.class,
                String.class,
                "com.yeecall.app.hep",
                char[].class,

                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "MasterCipher   :" + param.args[1]);
                    }

                });
        findAndHookMethod("com.yeecall.app.hes", lpparam.classLoader,
                "b",
                String.class,
                byte[].class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        byte[] bts = (byte[]) param.getResult();
                        String sss = new String(bts);
                        Log.d(TAG, "KeyValueStorageBase b byte[:  " + param.args[0] + ",int :" + sss);
                    }
                });

        findAndHookMethod("com.yeecall.app.hjy", lpparam.classLoader,
                "a",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
//                        Log.d(TAG, "KeyValueStorageBase:  " + param.args[0]+",int :"+param.args[1]);
                        Log.d(TAG, "数据库");
                        dumpStack();
                    }

                });
        findAndHookMethod("com.yeecall.app.hlx", lpparam.classLoader,
                "a",
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "ZayhuPref：" + (String) param.args[0] + "," + String.valueOf((int) param.args[1]));
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object obj = (Object) param.getResult();
                        Log.d(TAG, "ZayhuPref ret:" + obj.getClass().getName());
                    }
                });
        findAndHookMethod("com.yeecall.app.hjy", lpparam.classLoader,
                "a",
                char[].class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        char[] obj = (char[]) param.args[0];
                        char[] obj1 = (char[]) param.getResult();
                        String sss = String.valueOf(obj);
                        String ss = String.valueOf(obj1);
                        Log.d(TAG, "DBHelper input param:" + sss + ",ret: " + ss);
                    }
                });
        findAndHookMethod("com.yeecall.app.hen", lpparam.classLoader,
                "b",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //char[] obj = (char[]) param.args[0];
                        //char[] obj1=(char[])param.getResult();
                        //String sss = String.valueOf(obj);
                        //String ss=String.valueOf(obj1);
                        String sss = (String) param.args[0];
                        Log.d(TAG, "MasterCipher input param:" + sss + ",ret: ");
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {
                            dumpStack();
                        }
                    }
                });
        findAndHookMethod("com.yeecall.app.hee", lpparam.classLoader,
                "a",
                Context.class,
                String.class,
                char[].class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Context obj = (Context) param.args[0];
                        PackageInfo info = obj.getPackageManager().getPackageInfo(obj.getPackageName(), PackageManager.GET_SIGNATURES);
                        String sign = HexUtils.byteArr2Str(info.signatures[0].toByteArray());
                        FileUtils.writeFile(info.signatures[0].toByteArray(), "/sdcard/signature", false);
                        Log.d(TAG, "pack" + info.packageName + "signature: " + sign);
                        char[] obj1 = (char[]) param.args[2];
                        //String sss = String.valueOf(obj);
                        String ss = String.valueOf(obj1);
                        String sss = (String) param.args[1];
                        Log.d(TAG, "SecurePreferences input param:" + sss + "," + ss + ",int: " + param.args[3]);
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {

                            dumpStack();
                        }
                    }
                });
        findAndHookMethod("com.yeecall.app.heu", lpparam.classLoader,
                "a",
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        //char[] obj = (char[]) param.args[0];
                        //byte[] obj1=(byte[])param.getResult();
                        //String sss = String.valueOf(obj);
                        //String ss=String.valueOf(obj1);
                        String sss = (String) param.args[0];
                        Log.d(TAG, "PreferencesImpl input param:" + sss + ",int: " + param.args[1] + ",ret:");
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {

                            dumpStack();
                        }
                    }
                });
        findAndHookMethod("com.yeecall.app.hel", lpparam.classLoader,
                "a",

                String.class,

                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        //char[] obj = (char[]) param.args[0];
                        byte[] obj1 = (byte[]) param.getResult();
                        //String sss = String.valueOf(obj);
                        String ss = String.valueOf(obj1);
                        String sss = (String) param.args[0];
                        Log.d(TAG, "CipherProtocol input param:" + sss + ",int: " + param.args[1] + ",ret:" + ss);
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {

                            dumpStack();
                        }
                    }
                });
        //構造函數
        findAndHookConstructor("com.yeecall.app.heu", lpparam.classLoader,

                Context.class,
                String.class,
                char[].class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        //char[] obj = (char[]) param.args[0];
                        //byte[] obj1=(byte[])param.getResult();
                        //String sss = String.valueOf(obj);
                        //String ss=String.valueOf(obj1);
                        String sss = (String) param.args[1];
                        Log.d(TAG, "PreferencesImplC input param:" + sss + ",int: " + param.args[1] + ",ret:");
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {

                            dumpStack();
                        }
                    }
                });
        findAndHookMethod("com.yeecall.app.heu", lpparam.classLoader,
                "b",
                char[].class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        //char[] obj = (char[]) param.args[0];
                        //byte[] obj1=(byte[])param.getResult();
                        //String sss = String.valueOf(obj);
                        //String ss=String.valueOf(obj1);
                        char[] arga = (char[]) param.args[0];
                        String sss = new String(arga);

                        Log.d(TAG, "PreferencesImplb input param:" + sss + ",ret:" + param.getResult());
                        if (sss.equalsIgnoreCase("c_db_kvs_xxxxx")) {
                            dumpStack();
                        }
                    }
                });

    }
}


