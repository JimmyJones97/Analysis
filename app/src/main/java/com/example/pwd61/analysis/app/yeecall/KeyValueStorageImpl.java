package com.example.pwd61.analysis.app.yeecall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

import static com.example.pwd61.analysis.Utils.utils.Logd;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:KeyValueStorageImpl
 * Created by pwd61 on 2019/7/16 11:32
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public final class KeyValueStorageImpl extends KeyValueStorageBase {
    static int y;
    final PreferencesImpl A;
    final Object B = new Object();
//    final PreferencesCache C;
    final KeyValueDao keyValueDao;
    CipherBase E;
    boolean F = false;
    boolean G = false;
    boolean H = false;
    boolean I = false;
    boolean J = false;
    public boolean K = false;
    HashMap<String, WriteCacheEntry> L = new LinkedHashMap(256);
    HashMap<String, WriteCacheEntry> M = new LinkedHashMap(256);
    int N = 1;


    int O = 1;
    final a P = new a();
    final Object Q = new Object();
    String z;

    /* compiled from: KeyValueStorageImpl */
    class a implements Runnable {
        final SSProfiler a = new SSProfiler(1);
        volatile boolean b = false;
        volatile WriteCacheEntry[] c = new WriteCacheEntry[200];

        a() {
        }

        public void run() {
            try {
                if (LibraryConfig.b || KeyValueStorageImpl.this.I) {
                    this.a.a(0);
                }
                synchronized (KeyValueStorageImpl.this.Q) {
                    this.b = true;
                }
                int a = (KeyValueStorageImpl.this.F && KeyValueStorageImpl.this.H) ? a() : 0;
                synchronized (KeyValueStorageImpl.this.Q) {
                    this.b = false;
                }
                Arrays.fill(this.c, null);
                if (LibraryConfig.b || KeyValueStorageImpl.this.I) {
                    long b = this.a.b(0);
                    if (KeyValueStorageImpl.y > 0 && a > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("write ");
                        stringBuilder.append(KeyValueStorageImpl.this.z);
                        stringBuilder.append(":");
                        stringBuilder.append(a);
                        stringBuilder.append(" items in ");
                        stringBuilder.append(b);
                        stringBuilder.append("ms, avg=");
                        stringBuilder.append(b / ((long) a));
                        stringBuilder.append("ms, totalAvg=");
                        stringBuilder.append(this.a.c(0) / ((float) KeyValueStorageImpl.y));
                        stringBuilder.append("ms, totalCount=");
                        stringBuilder.append(KeyValueStorageImpl.y);
                        SystemAPI.a(stringBuilder.toString());
                    }
                }
            } catch (Throwable th) {
                int i;
                try {
                    if (LibraryConfig.b) {
                        th.printStackTrace();
                    }
                    synchronized (KeyValueStorageImpl.this.Q) {
                        this.b = false;
                        Arrays.fill(this.c, null);
                        if (LibraryConfig.b || KeyValueStorageImpl.this.I) {
                            this.a.b(0);
                            i = KeyValueStorageImpl.y;
                        }
                    }
                } catch (Throwable th2) {
                    synchronized (KeyValueStorageImpl.this.Q) {
                        this.b = false;
                        Arrays.fill(this.c, null);
                        if (LibraryConfig.b || KeyValueStorageImpl.this.I) {
                            this.a.b(0);
                            i = KeyValueStorageImpl.y;
                        }
                    }
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:73:0x00ec A:{SYNTHETIC, Splitter:B:73:0x00ec} */
        /* JADX WARNING: Removed duplicated region for block: B:77:0x00f5  */
        /* JADX WARNING: Missing block: B:40:0x007e, code skipped:
            if (r1 >= r6) goto L_0x00b8;
     */
        /* JADX WARNING: Missing block: B:42:?, code skipped:
            r3 = r11.c[r1];
     */
        /* JADX WARNING: Missing block: B:43:0x0084, code skipped:
            if (r3 == null) goto L_0x00b3;
     */
        /* JADX WARNING: Missing block: B:44:0x0086, code skipped:
            r3.d = r11.d.d(r3.a);
     */
        /* JADX WARNING: Missing block: B:45:0x0094, code skipped:
            if (r3.b == com.yeecall.app.hes.a) goto L_0x00b1;
     */
        /* JADX WARNING: Missing block: B:46:0x0096, code skipped:
            r3.e = r11.d.E.a(r3.c.a(r3.b), com.yeecall.app.het.a(r11.d, r3.a));
     */
        /* JADX WARNING: Missing block: B:47:0x00b1, code skipped:
            r3.e = null;
     */
        /* JADX WARNING: Missing block: B:48:0x00b3, code skipped:
            r1 = r1 + 1;
     */
        /* JADX WARNING: Missing block: B:49:0x00b6, code skipped:
            r0 = th;
     */
        /* JADX WARNING: Missing block: B:51:0x00b8, code skipped:
            com.yeecall.app.het.y += r6;
            r11.d.D.a(r11.c, r6);
     */
        /* JADX WARNING: Missing block: B:52:0x00c6, code skipped:
            if (r5 == null) goto L_0x00cf;
     */
        /* JADX WARNING: Missing block: B:54:?, code skipped:
            ((java.lang.Runnable) r5.b).run();
     */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <T> int a() throws Throwable {
            WriteCacheEntry hej;
            Throwable th;
            Throwable th2;
            Object obj = null;
            int i = 0;
            int i2;
            try {
                int i3;
                synchronized (KeyValueStorageImpl.this.B) {
                    try {
                        if (KeyValueStorageImpl.this.M.isEmpty()) {
                            return 0;
                        }
                        WriteCacheEntry hej2;
                        for (i2 = 0; i2 < this.c.length; i2++) {
                            this.c[i2] = null;
                        }
                        hej = null;
                        i3 = 0;
                        for (WriteCacheEntry hej22 : KeyValueStorageImpl.this.M.values()) {
                            try {
                                if (hej22.a != null || hej22.c != null || !(hej22.b instanceof Runnable)) {
                                    if (i3 >= 200) {
                                        break;
                                    }
                                    this.c[i3] = hej22;
                                    i3++;
                                } else {
                                    hej = hej22;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                obj = hej;
                                try {
                                    throw th;
                                } catch (Throwable th4) {
                                    hej = null;
                                    i2 = i;
                                    th2 = th4;
                                }
                            }
                        }
                        if (hej != null) {
                            KeyValueStorageImpl.this.M.remove(null);
                        }
                        for (i2 = 0; i2 < i3; i2++) {
                            WriteCacheEntry hej22 = this.c[i2];
                            if (hej22 != null) {
                                KeyValueStorageImpl.this.M.remove(hej22.a);
                            }
                        }
                        i2 = KeyValueStorageImpl.this.M.isEmpty() == false ? 1 : 0;
                        try {
                        } catch (Throwable th22) {
                            i = i2;
                            th = th22;
                            obj = hej;
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th;
                    }
                }
                if (i2 != 0) {
                    KeyValueStorageImpl.this.b(true);
                }
                return i3;
            } catch (Throwable th42) {
                hej = null;

                i2 = 0;
                if (hej != null) {
                    try {
                        ((Runnable) hej.b).run();
                    } catch (Throwable unused) {
                    }
                }
                if (i2 != 0) {
                    KeyValueStorageImpl.this.b(true);
                }
                throw th42;
            }
        }
    }

    public KeyValueStorageImpl(SecureDBHelper heq, PreferencesImpl heu, String str, int i) {
        if (i <= 0) {
            i = 1;
        }
        Logd("我错了？1");
//        this.C = new PreferencesCache(i);
        Logd("我错了？2");
        this.A = heu;
        this.z = str;
        this.keyValueDao = new KeyValueDao(heq, heu.d(), this.z);
    }

    public boolean c() {
        if (!this.F || this.H) {
            return this.G;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0073 A:{Catch:{ all -> 0x00a2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0097 A:{SYNTHETIC, Splitter:B:55:0x0097} */
    /* JADX WARNING: Missing block: B:20:0x0042, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:33:0x0065, code skipped:
            if (e(r2) == false) goto L_0x0067;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(CipherBase hek) {
        synchronized (this.A) {
            this.A.c();
        }
        synchronized (this) {
            this.H = false;
            if (hek == null) {
                this.F = true;
                notifyAll();
                return;
            }
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.z);
                stringBuilder.append("t26a26ebfab9b4e5f9f39784402706fd6efdf7081");
                String stringBuilder2 = stringBuilder.toString();
                this.E = hek;
                if (this.keyValueDao.a()) {
                    Object obj;
                    byte[] a = this.keyValueDao.getData(d(stringBuilder2));
                    if (a != null) {
                        if (!d(stringBuilder2, a)) {
                        }
                        obj = 1;
                        if (obj == null) {
                            SystemAPI.a("unable to load existing storage, reset it");
                            if (!this.keyValueDao.b()) {
                                this.F = true;
                                notifyAll();
                                return;
                            } else if (e(stringBuilder2)) {
                                obj = null;
                            } else {
                                this.F = true;
                                notifyAll();
                                return;
                            }
                        }
                        if (obj != null) {
                            this.H = true;
                            this.G = false;
                        }
                    }
                    obj = null;
                    if (obj == null) {
                    }
                    if (obj != null) {
                    }
                } else if (this.keyValueDao.b()) {
                    if (e(stringBuilder2)) {
                        this.H = true;
                        this.G = false;
                    } else {
                        this.F = true;
                        notifyAll();
                        return;
                    }
                }
                this.F = true;
                notifyAll();
            } finally {
                this.F = true;
                notifyAll();
            }
        }
    }

    private boolean d(String str, byte[] bArr) {
        byte[] b = this.E.b(bArr, f(str));
        boolean z = false;
        if (b == null) {
            return false;
        }
        try {
            if (TypeBytes.a(b) == 1) {
                z = true;
            }
            return z;
        } catch (Throwable th) {
            SystemAPI.a("failed to check storage version", th);
            return false;
        }
    }

    private boolean e(String str) {
        String d = d(str);
        byte[] a = this.E.a(TypeBytes.a(1), f(str));
        boolean z = false;
        if (a == null) {
            return false;
        }
        if (this.keyValueDao.a(d, a) > 0) {
            z = true;
        }
        return z;
    }

    private boolean d() {
        synchronized (this) {
            while (!this.F) {
                try {
                    if (LibraryConfig.b) {
                        SystemAPI.a("wait storage 1000ms ...");
                    }
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.H;
    }

    /* Access modifiers changed, original: 0000 */
    public String d(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.z);
        stringBuilder.append(str);
        return CipherProtocol.a(stringBuilder.toString());
    }

    private byte[] f(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.z);
        stringBuilder.append(str);
        return CipherProtocol.a(stringBuilder.toString(), 16);
    }

    /**
     * check database is close?
     */
    private void e() {
        StringBuilder stringBuilder;
        if (this.A.a()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SecurePreferences ");
            stringBuilder.append(this.A.d());
            stringBuilder.append(" is closed.");
            throw new RuntimeException(stringBuilder.toString());
        } else if (this.G) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.z);
            stringBuilder.append(" is closed.");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public boolean b(String str) {
        if (!d()) {
            return false;
        }
        e();
        synchronized (this.B) {
//            Object a = this.C.getv(str);
            if (a == a) {
                return false;
            } else if (a != null) {
                return true;
            } else {
                return this.keyValueDao.b(d(str));
            }
        }
    }

    /**
     * check 存的值是否相等
     *
     * @param str
     * @param t
     * @param bVar
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean a(String str, T t, b<?> bVar) {
        if (!d()) {
            return false;
        }
        e();//check databases close?
        synchronized (this.B) {
//            Object a = this.C.getv((Object) str);
            if (a != null && a != t && a.equals(t)) {
                return true;
            } else if (!(t == null || t == a)) {
                Object t2 = t;
                if (t2 == null) {
//                    this.C.a(str, a);
                } else {
//                    this.C.a(str, t2);
                }
                WriteCacheEntry a2 = WriteCacheEntry.a();
                a2.a = str;
                if (t2 == null) {
                    t2 = a;
                }
                a2.b = t2;
                a2.c = bVar;
                this.M.put(str, a2);

            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:25:0x0046, code skipped:
            if (r7 == false) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:26:0x0048, code skipped:
            return r5;
     */
    /* JADX WARNING: Missing block: B:27:0x0049, code skipped:
            r3.O++;
            r7 = r3.J;
     */
    /* JADX WARNING: Missing block: B:28:0x0053, code skipped:
            if (r3.J == false) goto L_0x0096;
     */
    /* JADX WARNING: Missing block: B:30:0x0059, code skipped:
            if (r3.O <= 10) goto L_0x0096;
     */
    /* JADX WARNING: Missing block: B:32:0x0060, code skipped:
            if ((r3.O / r3.N) <= 10) goto L_0x0096;
     */
    /* JADX WARNING: Missing block: B:33:0x0062, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Warning: cache miss rate too high for storage: ");
            r0.append(r3.z);
            r0.append(", key = ");
            r0.append(r4);
            r0.append(", cache hit = ");
            r0.append(r3.N);
            r0.append(", cache missed = ");
            r0.append(r3.O);
            android.util.Log.e("ST", r0.toString());
     */
    /* JADX WARNING: Missing block: B:34:0x0096, code skipped:
            r7 = r3.E.b(r3.D.a(d(r4)), f(r4));
            r0 = null;
     */
    /* JADX WARNING: Missing block: B:35:0x00ab, code skipped:
            if (r7 == null) goto L_0x00da;
     */
    /* JADX WARNING: Missing block: B:38:0x00b1, code skipped:
            r0 = r6.b(r7);
     */
    /* JADX WARNING: Missing block: B:39:0x00b3, code skipped:
            r6 = th;
     */
    /* JADX WARNING: Missing block: B:41:0x00b6, code skipped:
            if (com.yeecall.app.hdu.b != false) goto L_0x00b8;
     */
    /* JADX WARNING: Missing block: B:45:0x00c0, code skipped:
            r6 = r6.getCause();
     */
    /* JADX WARNING: Missing block: B:46:0x00c4, code skipped:
            r1 = new java.lang.StringBuilder();
            r1.append("ERROR: could not decode data for ");
            r1.append(r4);
            android.util.Log.e("ST", r1.toString(), r6);
     */
    /* JADX WARNING: Missing block: B:47:0x00da, code skipped:
            r6 = r3.C;
     */
    /* JADX WARNING: Missing block: B:48:0x00dc, code skipped:
            monitor-enter(r6);
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            r7 = r3.C.a((java.lang.Object) r4);
     */
    /* JADX WARNING: Missing block: B:51:0x00e5, code skipped:
            if (r7 != a) goto L_0x00e9;
     */
    /* JADX WARNING: Missing block: B:52:0x00e7, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:53:0x00e8, code skipped:
            return r5;
     */
    /* JADX WARNING: Missing block: B:54:0x00e9, code skipped:
            if (r7 == null) goto L_0x00ed;
     */
    /* JADX WARNING: Missing block: B:55:0x00eb, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:56:0x00ec, code skipped:
            return r7;
     */
    /* JADX WARNING: Missing block: B:57:0x00ed, code skipped:
            r7 = (com.yeecall.app.hej) r3.M.get(r4);
     */
    /* JADX WARNING: Missing block: B:58:0x00f5, code skipped:
            if (r7 == null) goto L_0x0103;
     */
    /* JADX WARNING: Missing block: B:59:0x00f7, code skipped:
            r7 = r7.b;
     */
    /* JADX WARNING: Missing block: B:60:0x00fb, code skipped:
            if (r7 != a) goto L_0x00ff;
     */
    /* JADX WARNING: Missing block: B:61:0x00fd, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:62:0x00fe, code skipped:
            return r5;
     */
    /* JADX WARNING: Missing block: B:63:0x00ff, code skipped:
            if (r7 == null) goto L_0x0103;
     */
    /* JADX WARNING: Missing block: B:64:0x0101, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:65:0x0102, code skipped:
            return r7;
     */
    /* JADX WARNING: Missing block: B:66:0x0103, code skipped:
            if (r0 == null) goto L_0x010c;
     */
    /* JADX WARNING: Missing block: B:67:0x0105, code skipped:
            r3.C.a(r4, r0);
     */
    /* JADX WARNING: Missing block: B:68:0x010a, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:69:0x010b, code skipped:
            return r0;
     */
    /* JADX WARNING: Missing block: B:70:0x010c, code skipped:
            r3.C.a(r4, a);
     */
    /* JADX WARNING: Missing block: B:71:0x0113, code skipped:
            monitor-exit(r6);
     */
    /* JADX WARNING: Missing block: B:72:0x0114, code skipped:
            return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Override
    public <T> T a(String str, T t, KeyValueStorageBase.a<?> aVar, boolean z) {
        SystemAPI.a("我进来了；");
        if (!d()) {
            return t;
        }
        e();
        synchronized (this.B) {
            boolean z2;
            Object a = null;
            if (a != null) {
                this.N++;
                z2 = this.J;
                if (a == a) {
                    return t;
                } else if (a != null) {
                    return (T)a;
                }
            }
            WriteCacheEntry hej = (WriteCacheEntry) this.M.get(str);
            if (hej != null) {
                a = hej.b;
                this.N++;
                z2 = this.J;
                if (a == a) {
                    return t;
                } else if (a != null) {
                    return (T) a;
                }
            }
        }
        return null;
    }

    public boolean b(boolean z) {
        return a(z, false);
    }

    public boolean a(boolean z, boolean z2) {
        synchronized (this.Q) {
            if (!z) {
                try {
                    if (!this.P.b) {
                    }
                } finally {
                }
            }
            this.P.b = true;
            if (z2) {
                SecureThread.a(this.P);
            } else {
                SecureThread.a(this.P, z ? 100 : 1000);
            }
        }
        return true;
    }

    public void c(String str) {
        synchronized (this.B) {
//            this.C.b(str);
        }
    }

    public void a() {
        synchronized (this.B) {
//            this.C.a();
        }
    }

    public void b() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        a(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
        }
    }

    /* JADX WARNING: Missing block: B:8:0x000b, code skipped:
            r0 = r4.B;
     */
    /* JADX WARNING: Missing block: B:9:0x000d, code skipped:
            monitor-enter(r0);
     */
    /* JADX WARNING: Missing block: B:12:0x0015, code skipped:
            if (r4.M.containsKey(null) == false) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:13:0x0017, code skipped:
            r1 = (com.yeecall.app.hej) r4.M.remove(null);
     */
    /* JADX WARNING: Missing block: B:14:0x001f, code skipped:
            if (r1 == null) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:15:0x0021, code skipped:
            r3 = (java.lang.Runnable) r1.b;
            r1.b();
     */
    /* JADX WARNING: Missing block: B:16:0x0029, code skipped:
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:17:0x002a, code skipped:
            r1 = new com.yeecall.app.het.AnonymousClass1(r4);
            r5 = com.yeecall.app.hej.a();
            r5.a = null;
            r5.b = r1;
            r5.c = null;
            r4.M.put(null, r5);
     */
    /* JADX WARNING: Missing block: B:18:0x003e, code skipped:
            monitor-exit(r0);
     */
    /* JADX WARNING: Missing block: B:19:0x003f, code skipped:
            a(true, true);
     */
    /* JADX WARNING: Missing block: B:20:0x0043, code skipped:
            return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(final CountDownLatch countDownLatch) {
        synchronized (this) {
            if (this.F) {
                if (!this.H) {
                }
            }
            countDownLatch.countDown();
        }
    }

    public void a(boolean z) {
        this.K = z;
    }

}
