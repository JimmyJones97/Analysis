package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.Process;

import java.lang.Thread.UncaughtExceptionHandler;

import static com.example.pwd61.analysis.Utils.Utils.Log;
import static com.example.pwd61.analysis.Utils.Utils.Logd;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SecureDBHelper
 * Created by pwd61 on 2019/7/12 13:58
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class SecureDBHelper extends SQLiteOpenHelper {
    private String dbName;

    /**
     * 打开数据库
     *
     * @param context 上下文
     * @param str     数据库路径
     */
    public SecureDBHelper(Context context, String str) {
        super(context, Environment.getExternalStorageDirectory().getPath() +'/'+ str, null, 1);
        this.dbName = str;
        Logd("数据库:" + Environment.getExternalStorageDirectory().getPath() +'/'+ str);
    }

    @Override
    /**
     * 创建表
     */
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("now create db: ");
        stringBuilder.append(sQLiteDatabase);
        Log(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.dbName);
        stringBuilder.append("tcfb3352c2df335696c6bc631932c6a61a4cdf318");
        String dbNameEnc = CipherProtocol.a(stringBuilder.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(dbNameEnc);
        stringBuilder2.append("t72f283666ae9a3482660515b0f9acebeaff91e04");
        String columnsID = CipherProtocol.a(stringBuilder2.toString());
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(dbNameEnc);
        stringBuilder3.append("te925705f61b25bfc077944de94029ec78ed12da0");
        String columnsID2 = CipherProtocol.a(stringBuilder3.toString());
        StringBuilder sqlstate = new StringBuilder();
        sqlstate.append("CREATE TABLE ");
        sqlstate.append(dbNameEnc);
        sqlstate.append(" (");
        sqlstate.append("_id");
        sqlstate.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlstate.append(columnsID);
        sqlstate.append(" TEXT UNIQUE, ");
        sqlstate.append(columnsID2);
        sqlstate.append(" BLOB);");
        Logd(sqlstate.toString());
        sQLiteDatabase.execSQL(sqlstate.toString());
    }

    public void a(SQLiteDatabase sQLiteDatabase, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("t72f283666ae9a3482660515b0f9acebeaff91e04");
        String a = CipherProtocol.a(stringBuilder.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append("te925705f61b25bfc077944de94029ec78ed12da0");
        String a2 = CipherProtocol.a(stringBuilder2.toString());
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(str);
        sql.append(" (");
        sql.append("_id");
        sql.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append(a);
        sql.append(" TEXT UNIQUE, ");
        sql.append(a2);
        sql.append(" BLOB);");
        sQLiteDatabase.execSQL(sql.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("CREATE UNIQUE INDEX  ");
        stringBuilder2.append(str);
        stringBuilder2.append("_index ON ");
        stringBuilder2.append(str);
        stringBuilder2.append(" (");
        stringBuilder2.append(a);
        stringBuilder2.append(");");
        sQLiteDatabase.execSQL(stringBuilder2.toString());
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("now upgrade db: ");
        stringBuilder.append(sQLiteDatabase);
        stringBuilder.append(", ");
        stringBuilder.append(i);
        stringBuilder.append(" --> ");
        stringBuilder.append(i2);
        Log(stringBuilder.toString());
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("now open db: ");
        stringBuilder.append(sQLiteDatabase);
        Log(stringBuilder.toString());
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("now configure db: ");
        stringBuilder.append(sQLiteDatabase);
        Log(stringBuilder.toString());
    }

    public void close() {
        CloseUitls.a(getWritableDatabase());
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sQLiteDatabase;
        sQLiteDatabase = null;
        Throwable th = null;
        for (int i = 0; i < 5 && th == null; i++) {
            try {
                sQLiteDatabase = super.getWritableDatabase();
            } catch (Throwable th2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unable to get writable database, wait for retry: ");
                stringBuilder.append(i);
                Log(stringBuilder.toString());
                th = th2;
                sQLiteDatabase = null;
            }
            if (sQLiteDatabase != null) {
                break;
            }
            try {
                Thread.sleep((long) ((200 << i) + 100));
            } catch (InterruptedException unused) {
            }
        }
        if (sQLiteDatabase == null) {
            //hfi.a("unable to get writable database, retry failed, restarting process", new Exception());
            UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (defaultUncaughtExceptionHandler != null) {
                if (th == null) {
                    defaultUncaughtExceptionHandler.uncaughtException(Thread.currentThread(), new Throwable("unable to load db, restart process ..."));
                } else {
                    defaultUncaughtExceptionHandler.uncaughtException(Thread.currentThread(), new Throwable("unable to load db, restart process ...", th));
                }
            }
            Process.killProcess(Process.myPid());
        }
        return sQLiteDatabase;
    }

}
