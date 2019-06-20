package com.example.pwd61.analysis.Detour;

import android.util.Log;


import com.example.pwd61.analysis.Detour.NetTools.monitor;
import com.example.pwd61.analysis.Detour.fuckApp.JD_anatomy;
import com.example.pwd61.analysis.Detour.fuckApp.ilongyuan;
import com.example.pwd61.analysis.Detour.fuckApp.neteaseMail;
import com.example.pwd61.analysis.Detour.fuckApp.pubg;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.*;

public class LetMeCrackU implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        final String TAG = "HACK";
        if (lpparam.packageName.equals("com.jingdong.app.mall")) {
            Log.w(TAG, "Start to fuck");
            JD_anatomy.doHook(lpparam);
        } else if (lpparam.packageName.equals("com.yixinjiang.goodbaba.app.presentation")) {
            Log.w(TAG, "handleLoadPackage: " + lpparam.packageName);
        } else if (lpparam.packageName.equals("com.tencent.tmgp.pubgmhd")) {
            Log.w(TAG, "let explorer UE4");
            pubg.doHook(lpparam);
        } else if (lpparam.packageName.equals("com.ilongyuan.sdorica.longyuan")) {
            Log.w(TAG, "let explorer ilongyuan");
            ilongyuan.doHook(lpparam);
        } else if (lpparam.packageName.equals("cmb.pb")) {
            Log.w(TAG, "let explorer 招商银行");
            //Zhaoshang.doHook(lpparam);
            monitor.doHook(lpparam);
        } else if (lpparam.packageName.equals("com.netease.mail")) {
            Log.w(TAG, "let explorer 网易邮箱");
            neteaseMail.doHook(lpparam);
        } else {
            Log.w(TAG, "Load package:" + lpparam.packageName + ",process:" + lpparam.processName);
        }
    }
}
