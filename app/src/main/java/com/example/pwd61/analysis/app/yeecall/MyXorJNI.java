package com.example.pwd61.analysis.app.yeecall;

import android.os.Process;
import android.util.Log;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:MyXorJNI
 * Created by pwd61 on 2019/7/15 14:08
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class MyXorJNI {
    private static final boolean a;

    public static native int xor(byte[] bArr, int bArr_index, int bArr_len, byte[] key, int key_index, int key_Len);

    static {
        boolean z;
        try {
            System.loadLibrary("sss");
            z = true;
        } catch (Throwable th) {
            Log.e("ERR", "unable to load jni library, this might happen during upgrading.kill self silently and wait for relaunch"+th.getMessage());
            Process.killProcess(Process.myPid());
            z = false;
        }
        a = z;
    }

}
