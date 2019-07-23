package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:IVCache
 * Created by pwd61 on 2019/7/16 9:18
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public final class IVCache extends LRUCache3<String, byte[]> {
    public IVCache() {
        super(512);
    }

}
