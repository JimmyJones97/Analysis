package com.example.pwd61.analysis.app.yeecall;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SecureThread
 * Created by pwd61 on 2019/7/16 9:04
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class SecureThread {
    private static boolean isDebug=false;
    private static Handler getMainLooper = new Handler(Looper.getMainLooper());
    private static HandlerThread secureThread = new HandlerThread("secure");
    private static Handler handler = new Handler(secureThread.getLooper());

    public static void a() {
        if (getMainLoop()) {
            throw new IllegalStateException("ensureNonUiThread: thread check failed");
        }
    }

    public static boolean getMainLoop() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

    static {
        secureThread.setPriority(3);
        secureThread.start();
    }

    public static void a(Runnable runnable) {
        if (isDebug) {
            handler.postAtFrontOfQueue(new ShowExceptionRunnable(runnable));
        } else {
            handler.postAtFrontOfQueue(runnable);
        }
    }

    public static void b(Runnable runnable) {
        if (isDebug) {
            handler.post(new ShowExceptionRunnable(runnable));
        } else {
            handler.post(runnable);
        }
    }

    public static void a(Runnable runnable, int i) {
        if (isDebug) {
            handler.postDelayed(new ShowExceptionRunnable(runnable), (long) i);
        } else {
            handler.postDelayed(runnable, (long) i);
        }
    }

}
