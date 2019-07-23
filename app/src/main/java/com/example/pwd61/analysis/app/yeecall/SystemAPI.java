package com.example.pwd61.analysis.app.yeecall;

import android.util.Log;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SystemAPI
 * Created by pwd61 on 2019/7/16 13:55
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class SystemAPI {
    public static void a(String str) {
        if (!LibraryConfig.a) {
            Log.i("YECALL", str);
        }
    }

    public static void a(String str, Throwable th) {
        if (!LibraryConfig.a) {
            Log.i("YECALL", str, th);
        }
    }

}
