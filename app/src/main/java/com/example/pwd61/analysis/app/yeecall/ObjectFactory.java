package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ObjectFactory
 * Created by pwd61 on 2019/7/16 14:26
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public abstract class ObjectFactory {
    protected static ObjectFactoryImpl a;

    public static boolean a() {
        synchronized (ObjectFactory.class) {
            if (a == null) {
                a = new ObjectFactoryImpl();
                a.b();
            }
        }
        return true;
    }

}
