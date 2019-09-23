package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import static com.example.pwd61.analysis.Utils.utils.Logd;

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
    protected static final Map<String, SecurePreferences> hashMap = new WeakHashMap();

    public abstract IKeyValueStorage a(String str, int i);

    public abstract boolean a();

    public static SecurePreferences a(Context context, String dbName, char[] ket) {
        return a(context, dbName, ket, 0);
    }

    public static SecurePreferences a(Context context, String dbName, char[] ket, int i) {
        SecurePreferences hee = null;
        if (context == null || TextUtils.isEmpty(dbName) || ket == null || ket.length == 0) {
            return null;
        }
        switch (i) {
            case 0:
            case 1:
                String dbNamer = dbName;
                dbNamer += ".sp";
//                File sdCard = Environment.getExternalStorageDirectory();
//                File databasePath = context.getDatabasePath(dbNamer);
                File file = new File(Environment.getExternalStorageDirectory(), dbNamer);
                if (i == 1 && (!file.exists() || !file.isFile())) {
                    return null;
                }
                synchronized (hashMap) {
                    SecurePreferences hee2 = (SecurePreferences) hashMap.get(dbNamer);
                    if (hee2 != null) {
                        if (!hee2.a()) {
                            if (!(hee2 instanceof PreferencesImpl) || ((PreferencesImpl) hee2).a(ket)) {
                                hee = hee2;
                            }
                        }
                    }
                    hee = new PreferencesImpl(context, dbNamer, ket, i);
                    hashMap.put(dbNamer, hee);
                }
                Logd("创建yeecall securePrefer成功");
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
