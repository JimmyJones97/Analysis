package com.example.pwd61.analysis.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:encrypt
 * Created by pwd61 on 2019/4/19 14:52
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class encrypt {
    public static String MD5_str(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            return MD5_bArr(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            //com.tencent.android.tpush.a.a.c("Tpush", "md5 encrypt:" + str, e);
        } catch (Exception e2) {
            //com.tencent.android.tpush.a.a.c(Constants.LogTag, "md5 encrypt:" + str, e2);
        }
        return "";
    }

    public static String MD5_bArr(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bArr) {
            stringBuilder.append(Integer.toHexString(b & 255));
        }
        return stringBuilder.toString();
    }
}
