package com.example.pwd61.analysis.WatchDog;

import android.content.Context;
import android.os.Environment;

import com.example.pwd61.analysis.Detour.Utils.MyLog;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:InfoHelper
 * Created by pwd61 on 2019/4/19 17:30
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class InfoHelper {
    public static boolean IsSDMount() {
        try {
            return "mounted".equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            MyLog.LogEt("FUCK", "isSDCardMounted", e);
            return false;
        }
    }
    public static String grow(String str, int i) {
        int length = str.length();
        if (length < i) {
            for (int i2 = 0; i2 < i - length; i2++) {
                str = str + " ";
            }
        }
        return str;
    }
    public static int checkDebug(Context context, String str, int i) {
        try {
            return 1;
        } catch (Exception e) {
            MyLog.LogEt("FUCK", "getInt", e);
            return 0;
        }
    }

}
