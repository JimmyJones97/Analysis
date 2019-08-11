package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.HashMap;

import static com.example.pwd61.analysis.Utils.Utils.Logd;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:PreferencesImpl
 * Created by pwd61 on 2019/7/16 11:32
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
// heu
public class PreferencesImpl extends SecurePreferences {
    volatile boolean b = false;
    private HashMap<String, KeyValueStorageImpl> c = new HashMap(64);
    private SecureDBHelper DBhelper = null;
    private Context ctx = null;
    private String settingstr;
    private File databasefile = null;
    private char[] encrytedstr = null;
    private String i = null;
    private int j = 0;
    private MasterCipher cipher;
    private boolean l = false;
    private boolean m = false;

    public PreferencesImpl(Context context, String str, char[] cArr, int i) {

        this.ctx = context.getApplicationContext();
        this.settingstr = str;
        //this.databasefile = context.getDatabasePath(this.settingstr);
        //* 把這個目錄換一下
        this.databasefile = new File(Environment.getExternalStorageDirectory(), this.settingstr);
        this.j = i;//0
        char[] cArr2 = new char[cArr.length];
        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
        this.encrytedstr = cArr2;
        this.i = b(cArr2);
        this.DBhelper = new SecureDBHelper(this.ctx, this.settingstr);
//        SecureThread.b(new Runnable() {
//            public void run() {
                PreferencesImpl.this.b();
//            }
//        });
    }

    /**
     * 同步数据库
     */
    public void b() {
        synchronized (this) {
            StringBuilder stringBuilder;
            try {
                boolean e = e();
                Logd("打开数据库是的是是是 是 是打算打赏埃斯蒂阿萨阿萨啊埃斯蒂阿萨  埃斯蒂阿萨阿萨 埃斯蒂埃斯蒂阿萨");
                this.l = true;
                this.m = !e;
                if (this.m) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("failed to load master container ");
                    stringBuilder.append(this.settingstr);
                    SystemAPI.a(stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("load ");
                    stringBuilder.append(this.settingstr);
                    stringBuilder.append(" success");
                    SystemAPI.a(stringBuilder.toString());
                }
                notifyAll();
                if (this.l && this.m) {
                    synchronized (a) {
                        a.remove(this.settingstr);
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();

            }
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void c() {
        while (!this.l) {
            try {
                SystemAPI.a("wait main container 1000ms ...");
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 打开数据库然后，检查数据库
     * @return true 打开成功
     * ，false打开失败
     */
    private boolean e() {
        if (!this.databasefile.exists() && this.j == 1) {
            return false;
        }
        MasterCipher masterCipher = new MasterCipher(this.ctx,
                this.settingstr,
                new KeyValueDao(
                        this.DBhelper,
                        this.settingstr,
                        "tcfb3352c2df335696c6bc631932c6a61a4cdf318"),
                CipherProtocol.a(this.ctx,
                        this.settingstr,
                        this.encrytedstr));
        if (!masterCipher.firstCheck()) {
            Logd("first check failed!");
            return false;
        }
        boolean b;
        try {
            b = masterCipher.b();
        } catch (Throwable th) {
            th.printStackTrace();
            b = false;
        }
        if (b) {
            this.cipher = masterCipher;
            return true;
        }
        String ms = "verify " + this.settingstr + " failed";
        Logd(ms);
        return false;
    }

    /**
     * 设置生成读写密钥
     *
     * @param str
     * @param het
     */
    private void a(final String str, final KeyValueStorageImpl het) {
        SecureThread.b(new Runnable() {
            public void run() {
                if (!PreferencesImpl.this.l || !PreferencesImpl.this.m) {
                    het.a(PreferencesImpl.this.cipher.b(str));
                }
            }
        });
    }

    /**
     * 返回对应的key value 读写类
     *
     * @param str
     * @param i
     * @return
     */
    public IKeyValueStorage a(String str, int i) {
        if (this.l && this.m) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ERROR: open storage ");
            stringBuilder.append(this.settingstr);
            stringBuilder.append("failed");
            SystemAPI.a(stringBuilder.toString());
            return null;
        } else if (this.b) {
            return null;
        } else {
            synchronized (this.c) {

                KeyValueStorageImpl het = (KeyValueStorageImpl) this.c.get(str);
                if (het == null || het.c()) {
                    het = new KeyValueStorageImpl(this.DBhelper, this, str, i);
                    a(str, het);//设置读写密钥
                    this.c.put(str, het);//缓存读写对象.
                    return het;
                }
                return het;
            }
        }
    }

    public boolean a() {
        if (this.l && this.m) {
            return true;
        }
        boolean z;
        synchronized (this) {
            z = this.b;
        }
        return z;
    }

    public boolean a(char[] cArr) {
        return (cArr == null || cArr.length == 0) ? false : b(cArr).equals(this.i);
    }

    /**
     * 追加a到对应字符数组
     *
     * @param cArr 输入数组
     * @return 返回加a字符串
     */
    public static String b(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        int length = cArr.length;
        byte[] bArr = new byte[(length * 2)];
        for (int i = 0; i < length; i++) {
            byte b = (byte) cArr[i];
            int i2 = i + i;
            bArr[i2] = b;
            bArr[i2 + 1] = (byte) (b >> 8);
        }
        return CipherProtocol.a('a', bArr);
    }

    public String d() {
        return this.settingstr;
    }

}
