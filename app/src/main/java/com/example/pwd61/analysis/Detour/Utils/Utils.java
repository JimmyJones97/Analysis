package com.example.pwd61.analysis.Detour.Utils;

import android.util.Log;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Utils
 * Created by pwd61 on 2019/3/26 14:12
 * description:这里存放很多工具函数，准备
 *
 *
 *
 *
 *
 ***************************************************************************/
public class Utils {
    //超级长的日志打印工具
    public static void Log(String veryLongString) {
        int maxLogSize = 1000;
        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.d("HACK", veryLongString.substring(start, end));
        }
    }

    //回溯调用栈
    public static void dumpStack() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dump Stack: " + "---------------start----------------");
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                sb.append(i == 0 ? "" : '\n' + "Dump Stack" + i + ": " + stackElements[i].getClassName()
                        + "----" + stackElements[i].getFileName()
                        + "----" + stackElements[i].getLineNumber()
                        + "----" + stackElements[i].getMethodName());
            }
        }
        sb.append("\nDump Stack: " + "---------------over----------------");
        Log.i("Dump Stack ", sb.toString());
    }
}
