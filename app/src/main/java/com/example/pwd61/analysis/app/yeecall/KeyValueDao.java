package com.example.pwd61.analysis.app.yeecall;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:KeyValueDao
 * Created by pwd61 on 2019/7/12 11:44
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class KeyValueDao {
    final Dao dao = new Dao();

    private final String anotherEnc;
    private final String columstr;
    private final String code;
    private final String encodeColum;
    private final String doubleEnc;
    private SQLiteDatabase database;
    private boolean sw = LibraryConfig.b;

    private SecureDBHelper dbHelper;
    private boolean l;


    static class Dao {
        SQLiteCursor cursor;
        String[] b = new String[1];
        int c = 0;
        Dao() {
        }
    }

    public KeyValueDao(SecureDBHelper dbHelper, String str, String code) {
        this.columstr = str;
        this.code = code;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.columstr);
        stringBuilder.append(code);
        this.encodeColum = CipherProtocol.a(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.encodeColum);
        stringBuilder.append("t72f283666ae9a3482660515b0f9acebeaff91e04");
        this.doubleEnc = CipherProtocol.a(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.encodeColum);
        stringBuilder.append("te925705f61b25bfc077944de94029ec78ed12da0");
        this.anotherEnc = CipherProtocol.a(stringBuilder.toString());
        this.dbHelper = dbHelper;
        this.l = false;
    }

    /***
     *
     * @return 返回數據庫
     */
    public SQLiteDatabase c() {
        if (this.database == null) {
            synchronized (this) {
                if (this.l) {
                    return null;
                } else if (this.database == null) {
                    this.database = this.dbHelper.getWritableDatabase();
                }
            }
        }
        return this.database;
    }

    /***
     *
     * @param aVar
     * @param str
     * @return 返回游標
     */
    private SQLiteCursor CreateCursor(Dao aVar, String str) {
        try {
            SQLiteDatabase c = c();
            String[] strArr = new String[]{this.anotherEnc};
            String stringBuilder=this.doubleEnc+"=?";//第二列
            aVar.b[0] = str;
            aVar.cursor = (SQLiteCursor) c.query(this.encodeColum, strArr, stringBuilder, aVar.b, null, null, null);
            aVar.c = aVar.cursor.getColumnIndex(this.anotherEnc);
            return aVar.cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     *
     * @param str 获取字段
     * @return 獲取結果
     */
    public byte[] getData(String str) {
        if (this.l) {
            return null;
        }
        byte[] c = null;
        synchronized (this.dao) {
            try {
                c = readCol(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    /**
     * 读数据库
     *
     * @param str 读字段
     * @return byte[]
     */
    private byte[] readCol(String str) {
        Dao aVar = this.dao;
        SQLiteCursor sqLiteCursor = aVar.cursor;
        if (sqLiteCursor == null) {
            CloseUitls.a(sqLiteCursor);
            CloseUitls.a((Cursor) sqLiteCursor);
            SQLiteCursor a = CreateCursor(aVar, str);
            aVar.cursor = a;
            if (a == null || !a.moveToFirst()) {
                return null;
            }
            return aVar.cursor.getBlob(aVar.c);

        }
        try {
            aVar.b[0] = str;
            sqLiteCursor.setSelectionArguments(aVar.b);
            sqLiteCursor.requery();
        } catch (Throwable uns) {
            CloseUitls.a(aVar.cursor);
            CloseUitls.a((Cursor) sqLiteCursor);
            aVar.cursor = sqLiteCursor;
        }
        if (sqLiteCursor == null || !sqLiteCursor.moveToFirst()) {
            return null;
        }
        return aVar.cursor.getBlob(aVar.c);
    }

    /**
     * 寫數據庫操作
     *
     * @param str
     * @param bArr
     * @return 返回是否成功
     */
    public long a(String str, byte[] bArr) {
        if (this.l) {
            return 0;
        }
        try {
            SQLiteDatabase c = c();
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.doubleEnc, str);
            contentValues.put(this.anotherEnc, bArr);
            return c.insert(this.encodeColum, null, contentValues);
        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    private boolean d(String str) {
        Dao aVar = this.dao;
        SQLiteCursor sQLiteCursor = aVar.cursor;
        boolean z = true;
        if (sQLiteCursor == null) {
            CloseUitls.a(aVar.cursor);
            CloseUitls.a((Cursor) sQLiteCursor);
            SQLiteCursor a = CreateCursor(aVar, str);
            aVar.cursor = a;
            if (a == null) {
                return false;
            }
            if (a.getCount() <= 0) {
                z = false;
            }
            return z;
        }
        try {
            aVar.b[0] = str;
            sQLiteCursor.setSelectionArguments(aVar.b);
            sQLiteCursor.requery();
        } catch (Throwable unused) {
            CloseUitls.a(aVar.cursor);
            CloseUitls.a((Cursor) sQLiteCursor);
            sQLiteCursor = CreateCursor(aVar, str);
            aVar.cursor = sQLiteCursor;
        }
        if (sQLiteCursor == null) {
            return false;
        }
        if (sQLiteCursor.getCount() <= 0) {
            z = false;
        }
        return z;
    }

    public boolean b(String str) {
        if (this.l) {
            return false;
        }
        boolean d = false;
        synchronized (this.dao) {
            try {
                d = d(str);
            } catch (Throwable th) {
            }
        }
        return d;
    }


    public boolean b() {
        boolean z;
        Exception e;
        Throwable th;
        SQLiteDatabase c;
        try {
            c = c();
            try {
                c.beginTransaction();
                this.dbHelper.a(c, this.encodeColum);
                c.setTransactionSuccessful();
                if (c != null) {
                    try {
                        c.endTransaction();
                    } catch (Throwable th2) {
                        z = LibraryConfig.b;
                        th2.printStackTrace();
                    }
                }
                return true;
            } catch (Exception e2) {
                e = e2;
                try {
                    if (this.sw) {
                        e.printStackTrace();
                    }
                    if (c != null) {
                        try {
                            c.endTransaction();
                        } catch (Throwable th3) {
                            boolean z2 = LibraryConfig.b;
                            th3.printStackTrace();
                        }
                    }
                    return false;
                } catch (Throwable th4) {

                    if (c != null) {
                        try {
                            c.endTransaction();
                        } catch (Throwable th22) {

                            th22.printStackTrace();
                        }
                    }
                    throw th4;
                }
            }
        } catch (Exception e3) {
            Exception exception = e3;
            c = null;
            e = exception;
            if (this.sw) {
            }
            if (c != null) {
            }
            return false;
        } catch (Throwable th222) {
            Throwable th5 = th222;
            c = null;

            if (c != null) {
            }
            throw th222;
        }
    }

    public boolean a() {
        try {
            return a(c(), this.encodeColum);
        } catch (Exception e) {
            if (this.sw) {
                e.printStackTrace();
            }
            return false;
        }
    }

    static boolean a(SQLiteDatabase sQLiteDatabase, String str) {
        Throwable th;
        Cursor cursor = null;
        try {
            String[] r2 = new String[2];
            boolean z = false;
            r2[0] = str;
            r2[1] = "table";
            Cursor rawQuery = sQLiteDatabase.rawQuery("select count(*) from sqlite_master where tbl_name=?  and type=?", r2);
            if (rawQuery != null) {
                try {
                    if (rawQuery.moveToNext()) {
                        if (rawQuery.getInt(0) > 0) {
                            z = true;
                        }
                        CloseUitls.a(rawQuery);
                        return z;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = rawQuery;
                    CloseUitls.a(cursor);
                    throw th;
                }
            }
            CloseUitls.a(rawQuery);
            return false;
        } catch (Throwable th3) {
            th = th3;
            CloseUitls.a(cursor);
            th.printStackTrace();
            return false;
        }
    }


}
