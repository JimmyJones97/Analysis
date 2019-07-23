package com.example.pwd61.analysis.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.Socket;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Utilities
 * Created by pwd61 on 2019/7/9 13:58
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class Utilities {
    static Context a;
    static String b;
    static String c;
    private static Boolean d;

    public static boolean a(String str) {
        try {
            File file = new File(str);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            return true;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ensureDirectory - ");
            stringBuilder.append(e);
            Log.e("Utilities", stringBuilder.toString());
            return false;
        }
    }

    public static void a(Context context) {
        a = context;
    }

    public static void a(Context context, String str, String str2) {
        a = context;
        b = str;
        c = str2;
    }

    public static Context a() {
        if (a != null) {
            return a;
        }
        throw new IllegalStateException("Common library is used before initialize!");
    }

    public static Object b(String str) {
        if (a != null) {
            return a.getSystemService(str);
        }
        throw new IllegalStateException("Common library is used before initialize!");
    }

    public static String b() {
        return b;
    }

    public static void c() {
//        hfr.a(new Runnable() {
//            public void run() {
//                hfu.l();
//                try {
//                    hcm.a();
//                } catch (Throwable th) {
//                    hcr.a("init missed api failed", th);
//                }
//                try {
//                    hds.a();
//                } catch (Throwable th2) {
//                    hcr.a("unable to init reflect based wallpaper manager", th2);
//                }
//            }
//        });
    }

    public static void d() {
        try {
            Class loadClass = ClassLoader.getSystemClassLoader().loadClass("dalvik.system.VMRuntime");
            Method method = loadClass.getMethod("getRuntime", new Class[0]);
            Method method2 = loadClass.getMethod("setMinimumHeapSize", new Class[]{Long.TYPE});
            Method method3 = loadClass.getMethod("setTargetHeapUtilization", new Class[]{Float.TYPE});
            int i = Build.VERSION.SDK_INT <= 10 ? 8 : 12;
            Object invoke = method.invoke(null, (Object[]) null);
            method2.invoke(invoke, new Object[]{Integer.valueOf((i * 1024) * 1024)});
            method3.invoke(invoke, new Object[]{Float.valueOf(0.85f)});
//            hcr.a("set vm parameters done");
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException unused) {
        }
    }

    public static boolean e() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(DatagramSocket datagramSocket) {
        if (datagramSocket != null) {
            try {
                datagramSocket.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(AssetFileDescriptor assetFileDescriptor) {
        if (assetFileDescriptor != null) {
            try {
                assetFileDescriptor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(String str, Object[] objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        if (str == null) {
            str = "";
        }
        stringBuilder.append(str);
        stringBuilder.append(":[");
        if (objArr != null && objArr.length > 0) {
            for (int i = 0; i < objArr.length - 1; i++) {
                stringBuilder.append(objArr[i]);
                stringBuilder.append(",");
            }
            stringBuilder.append(objArr[objArr.length - 1]);
        }
        stringBuilder.append("]");
//        hcr.a(stringBuilder.toString());
    }

}
