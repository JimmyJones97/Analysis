package com.example.pwd61.analysis.app.yeecall;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ObjectStoreImpl
 * Created by pwd61 on 2019/7/16 14:28
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class ObjectStoreImpl extends ObjectStore {
    HashMap<String, WeakReference<Object>> b = new HashMap();

    public void c() {
    }

    ObjectStoreImpl() {
    }

    public boolean a(String str, Object obj) {
        Object b = b(str);//取出来是否存在这个对象
        if (b != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("obj: ");
            stringBuilder.append(str);
            stringBuilder.append(" is already registered to ");
            stringBuilder.append(b);
            stringBuilder.append(". Old object will be over written");
            LOG.c(stringBuilder.toString(), new Exception());
        }

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(" --> ");
        stringBuilder2.append(obj);
        LOG.a(stringBuilder2.toString());

        synchronized (this.b) {
            this.b.put(str, new WeakReference(obj));
        }
        return true;
    }

    public boolean a(String str) {

        LOG.a(str);

        if (str == null) {
            return false;
        }
        synchronized (this.b) {
            this.b.remove(str);
        }
        return true;
    }

    public Object b(String str) {
        if (str != null) {
            WeakReference weakReference;
            synchronized (this.b) {
                weakReference = (WeakReference) this.b.get(str);
            }
            if (weakReference != null) {
                return weakReference.get();
            }
        }
        return null;
    }

}
