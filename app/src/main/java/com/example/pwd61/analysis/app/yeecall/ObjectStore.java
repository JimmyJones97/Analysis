package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ObjectStore
 * Created by pwd61 on 2019/7/16 14:26
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public abstract class ObjectStore {
    protected static ObjectStoreImpl a;

    public abstract boolean a(String str);

    public abstract boolean a(String str, Object obj);

    public abstract Object b(String str);

    public static boolean a() {
        synchronized (ObjectFactory.class) {
            if (a == null) {
                a = new ObjectStoreImpl();
                a.c();
            }
        }
        return true;
    }

    public static ObjectStore b() {
        if (a == null) {
            a();
        }
        return a;
    }

}
