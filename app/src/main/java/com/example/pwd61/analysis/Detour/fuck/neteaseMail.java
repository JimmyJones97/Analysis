package com.example.pwd61.analysis.Detour.fuck;

import android.util.Log;
import com.example.pwd61.analysis.Detour.tools.Utils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static com.example.pwd61.analysis.Detour.tools.Utils.dumpStack;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class neteaseMail {
    static private String TAG = "netease";
    static private boolean isFirstLoad = true;

    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("a.auu.a", lpparam.classLoader,
                "c",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String pa = (String) param.args[0];
                        if (pa.length() > 2) {
                            Log.d(TAG, "混淆前:" + pa + ",反混淆后: " + param.getResult());
                        }

                    }
                });
        findAndHookMethod("com.netease.mobimail.fragment.LoginFragment", lpparam.classLoader,
                "f",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        final String statusBar = (String) XposedHelpers.getObjectField(param.thisObject, "F");
                        Log.d(TAG, "登陆f:" + param.getResult() + ",密码:" + statusBar);
                        dumpStack();
                    }
                });

        findAndHookMethod("com.netease.mail.wzp.encrypt.RSAUtils", lpparam.classLoader,
                "encrypt",
                "java.security.Key",
                "java.nio.ByteBuffer",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        //String sss=JSON.toJSONString(param.args[0]);
                        //Utils.Log("存储类型:"+ConvJSON.putJSON("storage",param.args[0]));
                        ///Utils.Log("存储类型:"+sss);
//                        final String pwd = (String) XposedHelpers.getObjectField(param.thisObject, "F");
//                        Log.d(TAG, "登陆f:" + param.getResult() + ",密码:" + pwd);
                        dumpStack();
                        //SmartBarUtils.Exec("ls");
                        Utils.Log("神奇加密");
                    }
                });
        findAndHookMethod("com.netease.mobimail.util.au", lpparam.classLoader,
                "w",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        dumpStack();

                        Utils.Log("神奇");
                    }
                });
        //doLogin
        findAndHookMethod("com.netease.mobimail.fragment.PrefServerFragment", lpparam.classLoader,
                "doLogin",
                boolean.class,
                "com.netease.mobimail.storage.entity.b",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        dumpStack();

                        Utils.Log("神奇Login");
                    }
                });
    }
}
