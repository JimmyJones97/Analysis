package com.example.pwd61.analysis.app.yeecall;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:HashUtils
 * Created by pwd61 on 2019/7/9 11:29
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class HashUtils {
    public static String SHA1(String str) {
        try {
            return eNcrypt("SHA1", str.getBytes("utf8"));
        } catch (UnsupportedEncodingException unused) {
            return eNcrypt("SHA1", str.getBytes());
        }
    }
    private static String eNcrypt(String type, byte[] bArr) {
        return a(type, bArr, 0, bArr.length);
    }
    private static String a(String type, byte[] bArr, int i, int i2) {
        byte[] b = SHA(type, bArr, i, i2);
        if (b != null) {
            return stringlize(b);
        }
        return String.valueOf(bArr.hashCode());
    }
    private static byte[] SHA(String type, byte[] bArr, int i, int i2) {
        try {
            MessageDigest instance = MessageDigest.getInstance(type);
            instance.reset();
            instance.update(bArr, i, i2);
            return instance.digest();
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public static String stringlize(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            int i = b & 255;
            if (i < 16) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("0");
                stringBuilder.append(Integer.toHexString(i));
                stringBuffer.append(stringBuilder.toString());
            } else if (i >= 16) {
                stringBuffer.append(Integer.toHexString(i));
            }
        }
        return stringBuffer.toString();
    }




}
