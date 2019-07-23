package com.example.pwd61.analysis.app.yeecall;


import java.util.LinkedList;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:WriteCacheEntry
 * Created by pwd61 on 2019/7/16 13:41
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class WriteCacheEntry {
    private static final LinkedList<WriteCacheEntry> f = new LinkedList();
    public String a;
    public Object b;
    public KeyValueStorageBase.b<?> c;
    public String d;
    public byte[] e;
    private boolean g = false;

    private WriteCacheEntry() {
    }

    public static WriteCacheEntry a() {
        WriteCacheEntry hej;
        synchronized (f) {
            if (f.isEmpty()) {
                hej = null;
            } else {
                hej = (WriteCacheEntry) f.getFirst();
                f.removeFirst();
            }
        }
        if (hej == null) {
            hej = new WriteCacheEntry();
        }
        hej.g = false;
        return hej;
    }

    public void b() {
        if (!this.g) {
            this.g = true;
            this.a = null;
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = null;
            synchronized (f) {
                if (f.size() < 256) {
                    f.add(this);
                }
            }
        }
    }

}
