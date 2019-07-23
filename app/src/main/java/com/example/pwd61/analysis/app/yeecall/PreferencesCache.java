package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:PreferencesCache
 * Created by pwd61 on 2019/7/16 11:56
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class PreferencesCache  extends LRUCache3<String,Object>{
    public PreferencesCache() {
        super(100);
    }

    public PreferencesCache(int i) {
        super(i);
    }

}
