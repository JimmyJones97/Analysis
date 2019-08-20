package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;

import com.example.pwd61.analysis.Utils.utils;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ZayhuPref
 * Created by pwd61 on 2019/7/16 11:26
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class ZayhuPref {
    static SecurePreferences securePreferences;

    private static final char[] b = "50f4bb718e0772932cbc7342c6295b4826c353f67181e560c3375a5ccae62e9b".toCharArray();

    public static void a(Context context) {
        String ss=new String(b);
        ss+="zayhu.main.settings";
        utils.Log(ss);
        char[] sha1Arr = HashUtils.SHA1(ss).toCharArray();
        synchronized (ZayhuPref.class) {
            if (securePreferences != null && securePreferences.a()) {
                securePreferences = null;
            }
            if (securePreferences == null) {
                securePreferences = SecurePreferences.a(context, "yeecall", sha1Arr);
            }
        }
        if (securePreferences != null) {
            ObjectStore.b().a("zayhu.main.settings");//移除這個key
            ObjectStore.b().a("zayhu.main.settings", securePreferences);
        } else {
            ObjectStore.b().a("zayhu.main.settings");
            LOG.c("unable to init preferences, clear global instance");
        }
        ZayhuPref.a("zayhu.data.global.settings");

    }

    public static IKeyValueStorage a(String str) {
        IKeyValueStorage a;
        synchronized (ZayhuPref.class) {
            a = securePreferences.a(str);
        }
        return a;
    }

    public static IKeyValueStorage a(Context ctxd, String str, int i) {
        IKeyValueStorage a;
        synchronized (ZayhuPref.class) {
            if (securePreferences == null || securePreferences.a()) {
                a = null;
                a(ctxd);
            }
            a = securePreferences.a(str, i);
        }
        return a;
    }

}
