package com.example.pwd61.analysis.app.yeecall;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:LRUCache3
 * Created by pwd61 on 2019/7/12 9:25
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class LRUCache3<K, V> {
    private final LinkedHashMap<K, V> kvLinkedHashMap;
    private int b;
    private int Limited;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;

    public LRUCache3(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.Limited = i;
        this.kvLinkedHashMap = new LinkedHashMap(0, 0.75f, true);
    }
    public final synchronized V getv(Object k) {
        if (k == null) {
            try {
                throw new NullPointerException("key == null");
            } catch (Throwable th) {
            }
        } else {
            synchronized (this) {
                V obj = this.kvLinkedHashMap.get(k);
                if (obj != null) {
                    this.g++;
                    return obj;
                }
                this.h++;
            }
        }
        return null;
    }

    /**
     * 存入LRUCACHE
     * @param k
     * @param v
     * @return
     */
    public final synchronized V a(K k, V v) {
        V put;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.d++;
            this.b += c(k, v);
            put = this.kvLinkedHashMap.put(k, v);
            if (put != null) {
                this.b -= c(k, put);
            }
        }
        if (put != null) {
            a(false, k, put, v);
        }
        a(this.Limited);
        return put;
    }
    public synchronized void a(int i) {
        while (true) {
            synchronized (this) {
                if (this.b >= 0) {
                    if (this.kvLinkedHashMap.isEmpty() && this.b != 0) {
                        break;
                    } else if (this.b > i) {
                        if (this.kvLinkedHashMap.isEmpty()) {
                            break;
                        }
                        Entry entry = (Entry) this.kvLinkedHashMap.entrySet().iterator().next();
                        K key =(K) entry.getKey();
                        V value =(V) entry.getValue();
                        this.kvLinkedHashMap.remove(key);
                        this.b -= c(key, value);
                        this.f++;
                    }
                }
            }
        }
    }
    public final synchronized V b(K k) {
        V remove=null;
        if (k == null) {
            try {
                throw new NullPointerException("key == null");
            } catch (Throwable th) {
            }
        } else {
            synchronized (this) {
                remove = this.kvLinkedHashMap.remove(k);
                if (remove != null) {
                    this.b -= c(k, remove);
                }
            }
        }
        if (remove != null) {
            a(false, k, remove, null);
        }
        return remove;
    }
    public synchronized void a(boolean z, K k, V v, V v2) {
    }
    public synchronized V c(K k) {
        return null;
    }
    private synchronized int c(K k, V v) {
        int b;
        b = b(k, v);
        if (b < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Negative size: ");
            stringBuilder.append(k);
            stringBuilder.append("=");
            stringBuilder.append(v);
            throw new IllegalStateException(stringBuilder.toString());
        }
        return b;
    }
    public synchronized int b(K k, V v) {
        return 1;
    }
    public final synchronized void a() {
        a(-1);
    }

    public final synchronized String toString() {
        int i;
        i = this.g + this.h;
        i = i != 0 ? (this.g * 100) / i : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.Limited), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i)});
    }


}
