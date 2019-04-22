package com.example.pwd61.analysis.WatchDog;

import android.content.Context;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:GlobalVar
 * Created by pwd61 on 2019/4/19 15:08
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class GlobalVar {
    private static Context a = null;
    private static String b = "";

    public static void SetCtx(Context context) {
        if (context != null) {
            a = context;
            b = context.getPackageName();
        }
    }
    public static Context getCtx() {
        return a;
    }


}
