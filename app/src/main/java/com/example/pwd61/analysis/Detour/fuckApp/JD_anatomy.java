package com.example.pwd61.analysis.Detour.fuckApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.example.pwd61.analysis.Detour.verfiy.tester;
import com.example.pwd61.analysis.Utils.ConvJSON;
import com.example.pwd61.analysis.Utils.FileUtils;
import com.example.pwd61.analysis.Utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.pwd61.analysis.Utils.Utils.dumpStack;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class JD_anatomy {

    static private String TAG = "HACK";
    static private boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {

        findAndHookMethod("com.getkeepsafe.relinker.a.f", lpparam.classLoader,
                "bX",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.d(TAG, "relinker lib");

                    }
                });
        final Class<?> elfpare =
                XposedHelpers.findClass("com.getkeepsafe.relinker.a.f", lpparam.classLoader);
        findAndHookConstructor(elfpare, File.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        File fils = (File) param.args[0];
                        Log.d(TAG, "relinker file: " + fils.getName());
                    }
                }
        );
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
        findAndHookMethod("com.jd.verify.common.b.b", lpparam.classLoader,
                "onPageFinished",
                WebView.class,
                String.class,

                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "WebView Finished URL:  " + param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        findAndHookMethod("com.jd.verify.common.b", lpparam.classLoader,
                "c",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "計時器Timer堆棧:");
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        findAndHookMethod("com.jd.lib.un.utils.c", lpparam.classLoader,
                "getString",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        // this will be called before the clock was updated by the original method
                        Log.d("HACK", "beforeHookedMethod:args1 " + param.args[0] + "," + param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //param.;
                        Log.d("HACK", "return arg1:" + param.args[0] + ",arg2:" + param.args[1] + (String) param.getResult());
                    }
                });
        findAndHookMethod("com.jd.lib.un.utils.c.a", lpparam.classLoader,
                "putString",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "afterHookedMethod: putString");
                        //dumpStack();
                    }

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //dumpStack();
                        Log.d(TAG, "putString: args1" + param.args[0] + ",args2" + param.args[1]);

                    }
                });

        findAndHookMethod("jd.wjlogin_sdk.common.inland.WJLoginInland", lpparam.classLoader,
                "JDLoginWithPasswordNew",
                String.class,
                String.class,
                String.class,
                String.class,
                "jd.wjlogin_sdk.common.listener.OnLoginCallback",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("beforeHookedMethod: JDLoginWithPasswordNew: \n.account:" + param.args[0] +
                                ",pwMD5:" + param.args[1] +
                                "\nsessionID:" + param.args[2] +
                                "\nvt code:" + param.args[3]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });

        findAndHookMethod("jd.wjlogin_sdk.common.inland.WJLoginInland", lpparam.classLoader,
                "e",
                "jd.wjlogin_sdk.common.inland.WJLoginInland",
                String.class,
                String.class,
                String.class,
                "jd.wjlogin_sdk.common.listener.OnCommonCallback", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //super.beforeHookedMethod(param);
                        Log.d(TAG, "beforeHookedMethod WJLoginInland e : args1:" + param.args[1] + ",args1:" + param.args[2] + ",args2:" + param.args[3]);

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //super.afterHookedMethod(param);
                        Log.d(TAG, "WJLoginInland.e() afterHookedMethod: ");
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.util.w", lpparam.classLoader,
                "a",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "解密前--->：" + param.args[1] + "," + param.args[0]);
                        dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //super.afterHookedMethod(param);
                        Log.d(TAG, "解密后<---: " + new String((byte[]) param.getResult()));
                    }
                }

        );

        findAndHookMethod("jd.wjlogin_sdk.util.w", lpparam.classLoader,
                "a",
                byte[].class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "加密前--->：" + new String((byte[]) param.args[0]) + "," + param.args[1]);
                        //Log.d(TAG, "加密堆棧:");
                        FileUtils.writeFile(new String((byte[]) param.args[0]) + '\n', Environment.getExternalStorageDirectory().getPath() + "/orig.txt", true);
                        dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //super.afterHookedMethod(param);
                        Log.d(TAG, "加密后<---: " + param.getResult());
                    }
                }

        );

        findAndHookMethod("jd.wjlogin_sdk.util.e", lpparam.classLoader,
                "a",
                byte[].class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "base 64 前--->：" + new String((byte[]) param.args[0]));
                        //FileUtils.writeFile(new String((byte[]) param.args[0]) + '\n', Environment.getExternalStorageDirectory().getPath() + "/orig.txt", true);
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //super.afterHookedMethod(param);
                        byte[] bytess = (byte[]) param.args[0];
                        String res = new String(tester.c(bytess, 0, bytess.length, 2));
                        param.setResult(res);
                        Log.d(TAG, "base64 后<---:" + res);
                    }
                }

        );
        findAndHookMethod("jd.wjlogin_sdk.util.e", lpparam.classLoader,
                "b",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "反base 64 前--->：" + (String) param.args[0]);
                        //FileUtils.writeFile(new String((byte[]) param.args[0]) + '\n', Environment.getExternalStorageDirectory().getPath() + "/orig.txt", true);
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //super.afterHookedMethod(param);
                        String bytess = (String) param.args[0];
                        byte[] res = tester.a(bytess.getBytes(), 2);
                        param.setResult(res);
                        Log.d(TAG, "反base64 后<---:" + new String(res));
                    }
                }

        );

        findAndHookMethod("com.jd.verify.a.c", lpparam.classLoader,
                "a",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "App驗證信息: " + param.args[0]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        findAndHookMethod("com.jd.verify.a.c", lpparam.classLoader,
                "a",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "App驗證錯誤: " + param.args[0] + param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        findAndHookMethod("com.jd.verify.common.b", lpparam.classLoader,
                "b",/*
                Context.class,
                "com.jd.verify.View.d",
                "com.jd.verify.View.a",*/
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "beforeHookedMethod: 船艦webclient");
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }
        );
        /*
        findAndHookMethod(" com.jd.verify.a.c",
                lpparam.classLoader,
                "a",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //verify模塊的調試代碼
                        Log.d(TAG, "verify 1 info:"+param.args[0]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }
        );
        findAndHookMethod(" com.jd.verify.a.c",
                lpparam.classLoader,
                "a",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //verify模塊的調試代碼
                        Log.d(TAG, "verify 2 info:"+param.args[0]+","+param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }
        );
        */
        findAndHookMethod(" com.jd.verify.Verify",
                lpparam.classLoader,
                "a",
                String.class,
                Context.class,
                String.class,
                "com.jd.verify.CallBack",
                "com.jd.verify.View.IView",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //verify模塊的調試代碼
                        Log.d(TAG, "verify init str:" + param.args[0] + ",str2:" + param.args[2]);
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                }
        );


        //hook chrome  loadurl
        findAndHookMethod("android.webkit.WebView", lpparam.classLoader,
                "loadUrl",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        Log.d(TAG, "loadUrl:" + param.args[0]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        // hook jsinterface result
        findAndHookMethod("com.jd.verify.common.JSInterface", lpparam.classLoader,
                "deviceInfo",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "JSInterface return :" + param.getResult());
                    }
                });

        findAndHookMethod("com.jd.verify.common.JSInterface", lpparam.classLoader,
                "getFp",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "getFp return :" + param.getResult());
                    }
                });


        findAndHookMethod("com.jd.verify.common.JSInterface", lpparam.classLoader,
                "setFp",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(TAG, "setFp set:" + param.args[0]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                    }
                });

        //android.content.SharedPreferences.Editor
        findAndHookMethod("com.jingdong.jdsdk.utils.JDSharedPreferences.a", lpparam.classLoader,
                "putString",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("JDSharedPreferences:putString  :" + param.args[0] + ",:" + param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });


//        findAndHookMethod("logo.cr", lpparam.classLoader,
//                "b",
//                String.class,
//
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
//                        Utils.Log("logo.cr.b  :" + param.args[0]);
//                        //dumpStack();
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
//                        //Utils.Log("logo spf 创建成功?:" + ((Boolean) param.getResult() ? "成功" : "失败"));
//                    }
//                });

        findAndHookMethod("logo.cr", lpparam.classLoader,
                "c",
                String.class,

                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("logo.cr.c  :" + param.args[0]);
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("logo.cr.c spf 创建成功?:" + ((Boolean) param.getResult() ? "成功" : "失败"));
                    }
                });


        final Class<?> clazz =
                XposedHelpers.findClass("logo.cr", lpparam.classLoader);
        findAndHookConstructor(clazz,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("hook logo.cr ");
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });


        findAndHookMethod("com.jd.sec.utils.LoadDoor", lpparam.classLoader,
                "a",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("LoadDoor enc before: " + param.args[0]);
                        dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("LoadDoor enc after: " + param.getResult());
                    }
                });

        findAndHookMethod("com.jd.sec.utils.LoadDoor", lpparam.classLoader,
                "a",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Context ctx = (Context) param.args[0];
                        String classname = ctx.getApplicationContext().getApplicationInfo().className;
                        //Utils.Log("LoadDoor getToken before: " + classname);
                        //dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("LoadDoor getToken after: " + param.getResult());
                        //Object object=(Object)param.thisObject;

                        //XposedHelpers.callMethod("com.jd.sec.utils.LoadDoor","a",);


                    }
                });
        findAndHookMethod("com.jd.sec.utils.LoadDoor", lpparam.classLoader,
                "a",
                Context.class,
                String.class,
                String[].class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Context ctx = (Context) param.args[0];
                        String classname = ctx.getApplicationContext().getApplicationInfo().className;
                        String[] sArray = (String[]) param.args[2];

                        Utils.Log("LoadDoor checkFingers before: " + classname
                                + ",args: " + param.args[1]
                                + "-:" + sArray[0]
                                + "-:" + sArray[1]
                                + "-:" + sArray[2]
                                + "-:" + sArray[3]
                        );
                        dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("LoadDoor checkFingers after: " + param.getResult());
                    }
                });


        findAndHookMethod("com.jd.verify.View.ClickVerifyButton", lpparam.classLoader,
                "startAnimation",

                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        Utils.Log("startAnimation before: ");
                        dumpStack();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("LoadDoor checkFingers after: " + param.getResult());
                    }
                });
        findAndHookMethod("logo.cr", lpparam.classLoader,
                "b",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("logo.cr.b() return : " + param.getResult());
                        //dumpStack();
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.common.a", lpparam.classLoader,
                "c",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("client info return : " + ConvJSON.putJSON("clientinfo", param.getResult()));
                        //dumpStack();
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.common.a", lpparam.classLoader,
                "b",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("tlv_0x4   return a: " + ConvJSON.putJSON("tlv_0x4", param.getResult()));
                        //dumpStack();
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.common.a", lpparam.classLoader,
                "c",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("ClientInfo   return : " + ConvJSON.putJSON("ClientInfo", param.getResult()));
                        //dumpStack();
                    }
                });
        findAndHookMethod("com.jd.stat.security.f", lpparam.classLoader,
                "b",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("tlv_0x4   return f: " + ConvJSON.putJSON("tlv_0x4", param.getResult()));
                        //dumpStack();
                    }
                });
        findAndHookMethod(" com.jd.stat.common.r", lpparam.classLoader,
                "a",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        SharedPreferences spf = (SharedPreferences) param.getResult();
                        Utils.Log("jma_softfingerprint return : " + spf.getString("jma_softfingerprint", ""));
                        //dumpStack();
                    }
                });
        findAndHookMethod("com.jd.stat.network.f", lpparam.classLoader,
                "b",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("get soft key return : " + param.getResult().toString());
                        //dumpStack();
                    }
                });
        findAndHookMethod("com.jd.stat.network.e", lpparam.classLoader,
                "a",
                Map.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("kaishifasong");
                        if (param.args[0] != null) {
                            Map<String, String> maps = (Map<String, String>) param.args[0];
                            for (Map.Entry<String, String> entry : maps.entrySet()) {
                                Utils.Log("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            }
                        }
                        //dumpStack();
                    }


                });
        findAndHookMethod("jd.wjlogin_sdk.b.a", lpparam.classLoader,
                "b",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("登录报文解包:" + ConvJSON.putJSON("package", param.getResult()));
                        dumpStack();
                    }


                });
        findAndHookMethod("jd.wjlogin_sdk.common.inland.WJLoginInland", lpparam.classLoader,
                "sendMsgCodeForPhoneNumLogin4JD",
                String.class,
                String.class,
                String.class,
                String.class,
                "jd.wjlogin_sdk.common.listener.OnDataCallback",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("beforeHookedMethod: sendMsgCodeForPhoneNumLogin4JD: \n.str1:" +
                                param.args[0] +
                                ",str2:" + param.args[1] +
                                "\nstr3:" + param.args[2] +
                                "\nstr4:" + param.args[3]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
        findAndHookMethod("jd.wjlogin_sdk.common.inland.WJLoginInland", lpparam.classLoader,
                "checkMsgCodeForPhoneNumLogin4JD",

                String.class,
                String.class,
                String.class,
                "jd.wjlogin_sdk.common.listener.OnCommonCallback",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("beforeHookedMethod: checkMsgCodeForPhoneNumLogin4JD: \n.str1:" +
                                param.args[0] +
                                ",str2:" + param.args[1] +
                                "\nstr3:" + param.args[2]);
                    }

                });
        findAndHookMethod("jd.wjlogin_sdk.common.inland.WJLoginInland", lpparam.classLoader,
                "checkHistory4JDPhoneNumLoginNew",
                String.class,
                String.class,
                String.class,
                "jd.wjlogin_sdk.common.listener.OnCommonCallback",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("beforeHookedMethod: checkHistory4JDPhoneNumLoginNew: \n.str1:" +
                                param.args[0] +
                                ",str2:" + param.args[1] +
                                "\nstr3:" + param.args[2]);
                    }

                });
        findAndHookMethod("com.jingdong.common.utils.a.c", lpparam.classLoader,
                "getSalt",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Utils.Log("beforeHookedMethod: getSalt:  str1:" + new String((byte[]) param.getResult()));
                    }

                });
        findAndHookMethod("com.jingdong.common.entity.UserAddress", lpparam.classLoader,
                "toString",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Utils.Log("beforeHookedMethod: toString:  str1:" + (String) param.getResult());
                    }

                });
        findAndHookMethod("com.jingdong.common.entity.UserAddress", lpparam.classLoader,
                "parser",
                JSONObject.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        JSONObject jsonObject = (JSONObject) param.args[0];
                        Utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                    }
                });

        findAndHookMethod("com.jingdong.common.entity.UserInfo", lpparam.classLoader,
                "parser",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        Utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        Utils.Log("beforeHookedMethod: toString:  str1:" + (String) param.args[0]);
                    }
                });
        findAndHookMethod(" jd.wjlogin_sdk.util.y", lpparam.classLoader,
                "b",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
//                        JSONObject jsonObject = (JSONObject) param.args[0];
//                        Utils.Log("beforeHookedMethod: toString:  str1:" + jsonObject.toString());
                        dumpStack();
                        Utils.Log("beforeHookedMethod: toString:  str1:" + (String) param.args[0] + (String) param.args[1]);
                    }
                });

    }

    static {
        //動態加載
        Utils.Log("LOAD ourself so library!");
        //System.loadLibrary("native-lib");
        System.load("/data/data/com.example.pwd61.analysis.sepc_emu/lib/libnative_lib.so");
    }
}
