package com.example.pwd61.analysis.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.pwd61.analysis.WatchDog.GlobalVar;
import com.example.pwd61.analysis.WatchDog.InfoHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:MyLog
 * Created by pwd61 on 2019/4/19 15:05
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class MyLog {
    public static boolean a = false;
    public static String logDirectory = ("tencent" + File.separator + "TPush" + File.separator + "Logs");

    private static boolean bLogFile = false;
    private static Boolean isReportEnable = null;
    public static ArrayList<String> logArray = new ArrayList<String>();
    private static boolean sdIsMount = false;
    private static boolean t = false;
    private static String logpath = null;

    private static volatile ExecutorService Executor = Executors.newSingleThreadExecutor(new threadFac());

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd_HH:mm:ss_SSS");


    private static boolean b(int i) {
        return true;
    }

    public static boolean MyLog(Context context) {
        if (context == null) {
            return false;
        }
        if (isReportEnable == null) {
            if (InfoHelper.checkDebug(context, "_isReportEnable", -1) == 1) {
                isReportEnable = Boolean.valueOf(true);
            } else {
                isReportEnable = Boolean.valueOf(false);
            }
        }
        return isReportEnable.booleanValue();
    }

    public static void LogEt(String str, String str2, Throwable th) {
        if (a && b(6)) {
            Log.e("XINGE", "[" + str + "] " + str2, th);
        }
        enqueLog("ERROR", str, str2, th);
    }
    public static void LogEx(String str, String str2) {
        if (a && b(6)) {
            Log.e("XINGE", "[" + str + "] " + str2);
        }
        enqueLog("ERROR", str, str2, null);
    }

    private static void enqueLog(String str, String str2, String str3, Throwable th) {
        if (bLogFile || MyLog(GlobalVar.getCtx())) {
            if (str2 == null || str2.trim().equals("")) {
                str2 = "XGLogger";
            }
            String format = simpleDateFormat.format(new Date());
            if (str3 == null) {
                str3 = "";
            }
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str3), 256);
            String header = InfoHelper.grow("[" + str2 + "]", 24);
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    putInArr(format + " " + InfoHelper.grow(str, 5) + " " + header + " " + readLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (th != null) {
                StringWriter stringWriter = new StringWriter();
                th.printStackTrace(new PrintWriter(stringWriter));
                putInArr(format + " " + str + stringWriter.toString());
            }
        }
    }

    private static void putInArr(String str) {
        if (!t) {
            logArray.add(str);
            if (logArray.size() == 100) {
                ArrayList arrayList = logArray;
                logArray = new ArrayList<>();
                sdIsMount = InfoHelper.IsSDMount();
                if (sdIsMount) {
                    Log.v("XGLogger", "have writable external storage, write log file");
                    Sync2File(arrayList);
                    return;
                }
                Log.v("XGLogger", "no writable external storage");
            }
        }
    }

    public static void e() {
        try {
            String path = getlogPath();
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    int length = path.length() + 5;
                    int length2 = length + TimerMachine.a.length();
                    for (File file2 : file.listFiles()) {
                        try {
                            if (file2.isFile()) {
                                String absolutePath = file2.getAbsolutePath();
                                if (TimerMachine.a(TimerMachine.a(absolutePath.substring(length, length2)), 7)) {
                                    Log.d("XGLogger", "delete logs file " + absolutePath);
                                    file2.delete();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("XGLogger", "removeOldDebugLogFiles" + e);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            Log.e("XGLogger", "removeOldDebugLogFiles", e2);
        }
    }

    public static boolean getReportSw(Context context) {
        if (context == null) {
            return false;
        }
        if (isReportEnable == null) {
            if (InfoHelper.checkDebug(context, "_isReportEnable", -1) == 1) {
                isReportEnable = Boolean.valueOf(true);
            } else {
                isReportEnable = Boolean.valueOf(false);
            }
        }
        return isReportEnable.booleanValue();
    }


    private static void Sync2File(ArrayList arrayList) {
        if (getReportSw(GlobalVar.getCtx())) {
        }
        if (bLogFile) {
            try {
                Executor.execute(new TaskFunc(arrayList));
            } catch (Exception e) {
                Log.e("XGLogger", "savelog error", e);
            }
        }
    }

    public static String getlogPath() {
        if (logpath != null) {
            return logpath;
        }
        try {
            logpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + logDirectory;
            return logpath;
        } catch (Throwable th) {
            Log.e("XGLogger", "TLogger ->getFileNamePre", th);
            return null;
        }
    }


}
