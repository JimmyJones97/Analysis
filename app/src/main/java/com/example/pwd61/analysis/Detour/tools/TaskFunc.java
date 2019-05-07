package com.example.pwd61.analysis.Detour.tools;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:TaskFunc
 * Created by pwd61 on 2019/4/19 15:20
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class TaskFunc implements Runnable {
    final /* synthetic */ ArrayList a;

    TaskFunc(ArrayList arrayList) {
        this.a = arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0140 A:{Catch:{ Exception -> 0x0144 }} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Override
    public void run() {
        Throwable e;
        BufferedWriter bufferedWriter = null;
        try {
            String a = MyLog.getlogPath();
            if (a == null) {
                try {
                    MyLog.logArray.clear();
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    Log.e("XGLogger", "close file stream error", e2);
                    return;
                }
            }
            String str = a + File.separator + "log";
            String str2 = str + TraceFormat.STR_UNKNOWN + TimerMachine.a() + "_1.txt";
            File file = new File(str2);
            file.getParentFile().mkdirs();
            File file2 = file;
            String str3 = str2;
            int i = 2;
            while (file2.exists()) {
                str3 = str + TraceFormat.STR_UNKNOWN + TimerMachine.a() + "_" + i + ".txt";
                file2 = new File(str3);
                if (i > 10) {
                    Log.w("XGLogger", "Unexpected error here, so many existed error file.");
                    str2 = str3;
                    break;
                }
                i++;
            }
            str2 = str3;
            Log.v("XGLogger", "Write log file: " + file2.getName());
            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(str2));
            try {
                Iterator it = this.a.iterator();
                while (it.hasNext()) {
                    bufferedWriter2.write(((String) it.next()) + "\n");
                }
                try {
                    MyLog.logArray.clear();
                    if (bufferedWriter2 != null) {
                        bufferedWriter2.close();
                    }
                } catch (Exception e22) {
                    Log.e("XGLogger", "close file stream error", e22);
                }
            } catch (Exception e3) {
                e = e3;
                bufferedWriter = bufferedWriter2;
                try {
                    Log.e("XGLogger", "write logs to file error", e);
                    try {
                        MyLog.logArray.clear();
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                    } catch (Exception e222) {
                        Log.e("XGLogger", "close file stream error", e222);
                    }
                    MyLog.e();
                } catch (Throwable th) {
                    e = th;
                    try {
                        MyLog.logArray.clear();
                        if (bufferedWriter != null) {
                        }
                    } catch (Exception e4) {
                        Log.e("XGLogger", "close file stream error", e4);
                    }
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                bufferedWriter = bufferedWriter2;
                MyLog.logArray.clear();
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                throw e;
            }
            MyLog.e();
        } catch (Exception e5) {
            e = e5;
        }catch (Throwable ss)
        {
            Log.d("tpush",ss.getMessage());
        }
    }

}
