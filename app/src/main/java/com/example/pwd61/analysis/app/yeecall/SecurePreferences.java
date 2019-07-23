package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SecurePreferences
 * Created by pwd61 on 2019/7/16 11:26
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public abstract class SecurePreferences {
    protected static final Map<String, SecurePreferences> a = new WeakHashMap();

    public abstract IKeyValueStorage a(String str, int i);

    public abstract boolean a();

    public static SecurePreferences a(Context context, String str, char[] cArr) {
        return a(context, str, cArr, 0);
    }

    public static SecurePreferences a(Context context, String str, char[] cArr, int i) {
        SecurePreferences hee = null;
        if (context == null || TextUtils.isEmpty(str) || cArr == null || cArr.length == 0) {
            return null;
        }
        switch (i) {
            case 0:
            case 1:
                String stringBuilder = str;
                stringBuilder += ".sp";
                File sdCard = Environment.getExternalStorageDirectory();
                File databasePath = context.getDatabasePath(stringBuilder);
                File file = new File(Environment.getExternalStorageDirectory(), stringBuilder);
                if (i == 1 && (!file.exists() || !file.isFile())) {
                    return null;
                }
                synchronized (a) {
                    SecurePreferences hee2 = (SecurePreferences) a.get(stringBuilder);
                    if (hee2 != null) {
                        if (!hee2.a()) {
                            if (!(hee2 instanceof PreferencesImpl) || ((PreferencesImpl) hee2).a(cArr)) {
                                hee = hee2;
                            }
                        }
                    }
                    hee = new PreferencesImpl(context, stringBuilder, cArr, i);
                    a.put(stringBuilder, hee);
                }
                return hee;
            default:
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("Unknwon open flags: ");
                stringBuilder5.append(i);
                throw new RuntimeException(stringBuilder5.toString());
        }
    }

    public IKeyValueStorage a(String str) {
        return a(str, 100);
    }

}
