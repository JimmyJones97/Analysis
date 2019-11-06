package com.example.pwd61.analysis.Utils;

import com.android.tencent.qq.qq.Utils;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:injectJNI
 * Created by pwd61 on 11/6/2019 2:15 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class injectJNI {
    static {
        System.loadLibrary("native_lib");
    }

    static public String inject_Jni_Onload() {
        return !Utils.cec("10864017", "1234") ? "succ" : "failed!";
    }
}
