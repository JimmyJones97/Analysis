package com.example.pwd61.analysis.app.yeecall;


import android.util.Log;
import android.widget.TabHost;

import java.io.Externalizable;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:KeyValueStorageBase
 * Created by pwd61 on 2019/7/16 11:33
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public abstract class KeyValueStorageBase implements IKeyValueStorage {
    public static final Object a = new Object();
    static final b<?> b = new b<Integer>() {
        public byte[] a(Integer num) {
            return null;
        }
    };
    static final b<?> c = new b<Integer>() {
        public byte[] a(Integer num) {
            return TypeBytes.a(num.intValue());
        }
    };
    static final a<?> d = new a<Integer>() {
        /* renamed from: a */
        public Integer b(byte[] bArr) {
            return Integer.valueOf(TypeBytes.a(bArr));
        }
    };
    static final b<?> e = new b<Long>() {
        public byte[] a(Long l) {
            return TypeBytes.a(l.longValue());
        }
    };
    static final a<?> f = new a<Long>() {
        /* renamed from: a */
        public Long b(byte[] bArr) {
            return Long.valueOf(TypeBytes.b(bArr));
        }
    };
    static final b<?> g = new b<Float>() {
        public byte[] a(Float f) {
            return TypeBytes.a(f.floatValue());
        }
    };
    static final a<?> h = new a<Float>() {
        /* renamed from: a */
        public Float b(byte[] bArr) {
            return Float.valueOf(TypeBytes.c(bArr));
        }
    };
    static final b<?> i = new b<String>() {
        public byte[] a(String str) {
            return TypeBytes.a(str);
        }
    };
    static final a<?> j = new a<String>() {
        /* renamed from: a */
        public String b(byte[] bArr) {
            return TypeBytes.d(bArr);
        }
    };
    static final b<?> k = new b<byte[]>() {
        public byte[] a(byte[] bArr) {
            return bArr;
        }
    };
    static final a<?> l = new a<byte[]>() {
        /* renamed from: a */
        public byte[] b(byte[] bArr) {
            return bArr;
        }
    };
    static final b<?> m = new b<Serializable>() {
        public byte[] a(Serializable serializable) {
            try {
                return TypeBytes.a(serializable);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
    };
    static final a<?> n = new a<Serializable>() {
        /* renamed from: a */
        public Serializable b(byte[] bArr) {
            try {
                return TypeBytes.e(bArr);
            } catch (Throwable E) {
                E.printStackTrace();
                return null;
            }
        }
    };
    static final b<?> o = new b<Externalizable>() {
        public byte[] a(Externalizable externalizable) {
            try {
                return TypeBytes.a(externalizable);
            } catch (Throwable tb) {
                tb.printStackTrace();
                return  null;
            }
        }
    };
    static final a<?> p = new a<Externalizable>() {
        /* renamed from: a */
        public Externalizable b(byte[] bArr) {
            try {
                return TypeBytes.f(bArr);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
    };
    static final b<?> q = new b<Serializable>() {
        public byte[] a(Serializable serializable) {
            try {
                return TypeBytes.b(serializable);
            }catch (Throwable tb)
            {
                tb.printStackTrace();
                return null;
            }
        }
    };
    static final a<?> r = new a<Serializable>() {
        /* renamed from: a */
        public Serializable b(byte[] bArr) {
            try {
                return TypeBytes.g(bArr);
            }catch (Throwable tbn )
            {
                tbn.printStackTrace();
                return null;
            }
        }
    };
    static final b<?> s = new b<Externalizable>() {
        public byte[] a(Externalizable externalizable) {
            try {
                return TypeBytes.b(externalizable);
            }catch (Throwable tb)
            {
                tb.printStackTrace();
                return null;
            }
        }
    };
    static final a<?> t = new a<Externalizable>() {
        /* renamed from: a */
        public Externalizable b(byte[] bArr) {

            try {
                return TypeBytes.h(bArr);
            }catch (Throwable TB)
            {
                TB.printStackTrace();
                return null;
            }
        }
    };
    static final b<?> u = new b<JSONArray>() {
        public byte[] a(JSONArray jSONArray) {
            return TypeBytes.a(jSONArray);
        }
    };
    static final a<?> v = new a<JSONArray>() {
        /* renamed from: a */
        public JSONArray b(byte[] bArr) {
            return TypeBytes.i(bArr);
        }
    };
    static final b<?> w = new b<JSONObject>() {
        public byte[] a(JSONObject jSONObject) {
            return TypeBytes.a(jSONObject);
        }
    };
    static final a<?> x = new a<JSONObject>() {
        /* renamed from: a */
        public JSONObject b(byte[] bArr) {
            return TypeBytes.j(bArr);
        }
    };

    /* compiled from: KeyValueStorageBase */
    public interface a<T> {
        T b(byte[] bArr);
    }

    /* compiled from: KeyValueStorageBase */
    public interface b<T> {
        byte[] a(T t);
    }

    public abstract <T> T a(String str, T t, a<?> aVar, boolean z);

    public abstract <T> boolean a(String str, T t, b<?> bVar);

    public boolean a(String str) {
        return a(str, a, b);
    }

    public boolean a(String str, int i) {
        return a(str, Integer.valueOf(i), c);
    }

    public int b(String str, int i) {
        return ((Integer) a(str, Integer.valueOf(i), d, false)).intValue();
    }

    public int c(String str, int i) {
        return ((Integer) a(str, Integer.valueOf(i), d, true)).intValue();
    }

    public boolean a(String str, long j) {
        return a(str, Long.valueOf(j), e);
    }

    public long b(String str, long j) {
        return ((Long) a(str, Long.valueOf(j), f, false)).longValue();
    }

    public long c(String str, long j) {
        return ((Long) a(str, Long.valueOf(j), f, true)).longValue();
    }

    public boolean a(String str, float f) {
        return a(str, Float.valueOf(f), g);
    }

    public float b(String str, float f) {
        return ((Float) a(str, Float.valueOf(f), h, false)).floatValue();
    }

    public float c(String str, float f) {
        return ((Float) a(str, Float.valueOf(f), h, true)).floatValue();
    }

    public boolean a(String str, String str2) {
        return a(str, str2, i);
    }

    public String b(String str, String str2) {
        return (String) a(str, str2, j, false);
    }

    public String c(String str, String str2) {
        return (String) a(str, str2, j, true);
    }

    public boolean a(String str, boolean z) {
        return a(str, z == false ? 0 : 1);
    }

    public boolean b(String str, boolean z) {
        int b = b(str, -1);
        if (b == -1) {
            return z;
        }
        return b == 1;
    }

    public boolean c(String str, boolean z) {
        int c = c(str, -1);
        if (c == -1) {
            return z;
        }
        return c == 1;
    }

    public boolean a(String str, byte[] bArr) {
        return a(str, bArr, k);
    }

    public byte[] b(String str, byte[] bArr) {
        Log.d("HACK","CAONIMA ");
        return (byte[]) a(str, bArr, l, false);
    }

    public byte[] c(String str, byte[] bArr) {
        return (byte[]) a(str, bArr, l, true);
    }

    public boolean a(String str, Serializable serializable) {
        return a(str, serializable, m);
    }

    public Serializable b(String str, Serializable serializable) {
        return (Serializable) a(str, serializable, n, false);
    }

    public Serializable c(String str, Serializable serializable) {
        return (Serializable) a(str, serializable, n, true);
    }

    public boolean a(String str, Externalizable externalizable) {
        return a(str, externalizable, o);
    }

    public Externalizable b(String str, Externalizable externalizable) {
        return (Externalizable) a(str, externalizable, p, false);
    }

    public Externalizable c(String str, Externalizable externalizable) {
        return (Externalizable) a(str, externalizable, p, true);
    }

    public boolean d(String str, Serializable serializable) {
        return a(str, serializable, q);
    }

    public Serializable e(String str, Serializable serializable) {
        return (Serializable) a(str, serializable, r, false);
    }

    public boolean d(String str, Externalizable externalizable) {
        return a(str, externalizable, s);
    }

    public Externalizable e(String str, Externalizable externalizable) {
        return (Externalizable) a(str, externalizable, t, false);
    }

    public boolean a(String str, JSONObject jSONObject) {
        return a(str, jSONObject, w);
    }

    public JSONObject b(String str, JSONObject jSONObject) {
        return (JSONObject) a(str, jSONObject, x, false);
    }

}
