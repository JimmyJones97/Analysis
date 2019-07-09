package com.example.pwd61.analysis.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Md5Encrypt
 * Created by pwd61 on 2019/5/30 10:08
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class Md5Encrypt {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            try {
                instance.update(str.getBytes("utf-8"));
                return new String(encodeHex(instance.digest()));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("System doesn't support your  EncodingException.");
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }
    }

    public static char[] encodeHex(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            /***********************************
             *  240这个值特殊化过，所以要用要求修改
             *  ##########################|
            * ****************************v*****/
            cArr[i] = DIGITS[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = DIGITS[bArr[i2] & 15];
        }
        return cArr;
    }

}
