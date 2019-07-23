package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:EncodeUtils
 * Created by pwd61 on 2019/7/15 16:00
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class EncodeUtils {
    public static void a(char[] cArr) {
        if (cArr != null && cArr.length != 0) {
            for (int i = 0; i < cArr.length; i++) {
                cArr[i] = 0;
            }
        }
    }

}
