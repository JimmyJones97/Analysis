package com.example.pwd61.analysis.app.cmb;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:EncryptUtil
 * Created by pwd61 on 2019/8/23 10:19
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class EncryptUtil {
    public native String decryptcbc(String str);
    static {
        System.loadLibrary("wb");
        //System.loadLibrary(StringObfuse.decode("140F"));
    }

}
