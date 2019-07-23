package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:HashCache
 * Created by pwd61 on 2019/7/16 9:28
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class HashCache extends LRUCache3<String, String> {
    public HashCache(){
        super(512);
    }
}
