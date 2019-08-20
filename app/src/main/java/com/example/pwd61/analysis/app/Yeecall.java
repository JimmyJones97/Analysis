package com.example.pwd61.analysis.app;

import android.content.Context;
import android.text.TextUtils;

import com.example.pwd61.analysis.app.yeecall.IKeyValueStorage;
import com.example.pwd61.analysis.app.yeecall.ZayhuPref;

import java.io.File;

import static com.example.pwd61.analysis.Utils.utils.Logd;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Yeecall
 * Created by pwd61 on 2019/7/9 11:52
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class Yeecall {
    private static final char[] b = "50f4bb718e0772932cbc7342c6295b4826c353f67181e560c3375a5ccae62e9b".toCharArray();

    //    static SecurePreferences a;
    public static void getKey( Context ctx) {
        String kvs = "c_db_kvs_xxxxx";
        Logd("start !");
        IKeyValueStorage IKeyValueStorage_inst = ZayhuPref.a(ctx, kvs, 5);
//        IKeyValueStorage_inst.b("slat","".getBytes());


    }

    public static void a(Context context, String str, char[] cArr, int i) {
        if (context == null || TextUtils.isEmpty(str) || cArr == null || cArr.length == 0) {
//            return null;
        }
        switch (i) {
            case 0:
            case 1:
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(".sp");
                File databasePath = context.getDatabasePath(sb.toString());
                if (i == 1 && (!databasePath.exists() || !databasePath.isFile())) {
//                    return null;
                }
//                Map
        }

    }

}
