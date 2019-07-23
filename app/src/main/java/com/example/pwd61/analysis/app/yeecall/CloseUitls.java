package com.example.pwd61.analysis.app.yeecall;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:CloseUitls
 * Created by pwd61 on 2019/7/12 11:48
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class CloseUitls {

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                if (closeable instanceof Cursor) {
                    ((Cursor) closeable).close();
                } else {
                    closeable.close();
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase != null) {
            try {
                sQLiteDatabase.close();
            } catch (Throwable unused) {
            }
        }
    }

}
