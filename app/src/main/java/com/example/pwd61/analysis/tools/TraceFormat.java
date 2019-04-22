package com.example.pwd61.analysis.tools;

import android.text.format.Time;
import android.util.Log;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:TraceFormat
 * Created by pwd61 on 2019/4/19 15:32
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

public final class TraceFormat {
    public static final TraceFormat DEFAULT = new TraceFormat();
    public static final String STR_ASSERT = "A";
    public static final String STR_DEBUG = "D";
    public static final String STR_ERROR = "E";
    public static final String STR_INFO = "I";
    public static final String STR_UNKNOWN = "-";
    public static final String STR_VERBOSE = "V";
    public static final String STR_WARN = "W";
    public static final String TRACE_TIME_FORMAT = "%Y-%m-%d %H:%M:%S";

    public final String getLevelPrefix(int i) {
        switch (i) {
            case 1:
                return STR_VERBOSE;
            case 2:
                return STR_DEBUG;
            case 4:
                return STR_INFO;
            case 8:
                return STR_WARN;
            case 16:
                return STR_ERROR;
            case 32:
                return STR_ASSERT;
            default:
                return STR_UNKNOWN;
        }
    }

    public String formatTrace(int i, Thread thread, long j, String str, String str2, Throwable th) {
        long j2 = j % 1000;
        Time time = new Time();
        time.set(j);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLevelPrefix(i)).append('/').append(time.format(TRACE_TIME_FORMAT)).append('.');
        if (j2 < 10) {
            stringBuilder.append("00");
        } else if (j2 < 100) {
            stringBuilder.append('0');
        }
        stringBuilder.append(j2).append(' ').append('[');
        if (thread == null) {
            stringBuilder.append("N/A");
        } else {
            stringBuilder.append(thread.getName());
        }
        stringBuilder.append(']').append('[').append(str).append(']').append(' ').append(str2).append(10);
        if (th != null) {
            stringBuilder.append("* Exception : \n").append(Log.getStackTraceString(th)).append(10);
        }
        return stringBuilder.toString();
    }
}
