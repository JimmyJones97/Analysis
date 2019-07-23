package com.yeecall.library.securestorage.xor;

import android.util.Log;
import android.os.Process;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:XorJNI
 * Created by pwd61 on 2019/7/15 14:08
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class XorJNI {
    private static final boolean a;

    public static native int xor(byte[] bArr, int i, int i2, byte[] bArr2, long j, int i3);

    static {
        boolean z;
        try {
            System.loadLibrary("ss");
            Log.d("HACK", "static initializer: 加载ss库 !!!");
            z = true;
        } catch (Throwable th) {
            Log.e("ERR", "unable to load jni library, this might happen during upgrading.kill self silently and wait for relaunch", th);
            Process.killProcess(Process.myPid());
            z = false;
        }
        a = z;
    }

}
