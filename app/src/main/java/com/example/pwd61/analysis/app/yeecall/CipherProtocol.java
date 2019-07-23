package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:CipherProtocol
 * Created by pwd61 on 2019/7/12 9:09
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class CipherProtocol {
    static final HashCache cache = new HashCache();
    static final IVCache b = new IVCache();

    static final ThreadLocal<MessageDigest> threadlocal = new ThreadLocal();
    static final char[] d = "0123456789abcdef".toCharArray();
    private static SecureRandom e = new SecureRandom();

    public static String a(String str) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = (MessageDigest) threadlocal.get();
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("SHA1");
            } catch (Exception unused) {
                unused.printStackTrace();
            }
            threadlocal.set(messageDigest);
        } else {
            messageDigest.reset();
        }
        if (messageDigest == null) {
            return null;
        }
        //StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append(str);
        //stringBuilder.append("t4f0e200dd0c8a04f39a2144fe119c83d5f37fc7c");
        String sb = str + "t4f0e200dd0c8a04f39a2144fe119c83d5f37fc7c";
        byte[] b = messageDigest.digest(Str2byteArr(sb));
        Log.d("HACK", "参数" + sb + ", 加密: " + HexUtils.byteArr2Str(b));
        String encryp = aa('a', b);
        synchronized (cache) {
            //cache.a(str, encryp);/// LRU cache
        }
        return encryp;
    }

    /**
     * append a
     * @param c
     * @param bArr
     * @return
     */
    public static String aa(char c, byte[] bArr) {
        char[] cArr = new char[((bArr.length * 2) + 1)];
        int i = 0;
        cArr[0] = c;
        while (i < bArr.length) {
            int i2 = bArr[i] & 255;
            int i3 = (i << 1) + 1;
            cArr[i3] = d[i2 >>> 4];
            cArr[i3 + 1] = d[i2 & 15];
            i++;
        }
        return new String(cArr);
    }

    public static byte[] a(String str, int i) {
        if (str == null) {
            return null;
        }
        byte[] bArr;
        synchronized (b) {
            bArr = (byte[]) b.getv(str);
        }
        byte[] bArr2;
        if (bArr != null) {
            bArr2 = new byte[i];
            if (bArr.length >= i) {
                System.arraycopy(bArr, 0, bArr2, 0, i);
            } else {
                Arrays.fill(bArr2, (byte) 0);
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            }
            return bArr2;
        }
        MessageDigest messageDigest = (MessageDigest) threadlocal.get();
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException unused) {
            }
            threadlocal.set(messageDigest);
        } else {
            messageDigest.reset();
        }
        if (messageDigest == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("t7979dcdb93d0cf37ce4fdf796230da10138e2383");
        byte[] digest = messageDigest.digest(Str2byteArr(stringBuilder.toString()));
        synchronized (b) {
            b.a(str, digest);
        }
        bArr2 = new byte[i];
        if (digest.length >= i) {
            System.arraycopy(digest, 0, bArr2, 0, i);
        } else {
            Arrays.fill(bArr2, (byte) 0);
            System.arraycopy(digest, 0, bArr2, 0, digest.length);
        }
        return bArr2;
    }

    private static byte[] Str2byteArr(String str) {
        char[] toCharArray = str.toCharArray();
        int length = toCharArray.length;
        byte[] bArr = new byte[(length * 2)];
        for (int i = 0; i < length; i++) {
            byte b = (byte) toCharArray[i];
            int i2 = i + i;
            bArr[i2] = b;
            bArr[i2 + 1] = (byte) (b >> 8);
        }
        return bArr;
    }

    public static String a(char c, byte[] bArr) {
        char[] cArr = new char[((bArr.length * 2) + 1)];
        int i = 0;
        cArr[0] = c;
        while (i < bArr.length) {
            int i2 = bArr[i] & 255;
            int i3 = (i << 1) + 1;
            cArr[i3] = d[i2 >>> 4];
            cArr[i3 + 1] = d[i2 & 15];
            i++;
        }
        return new String(cArr);
    }

    public static byte[] a(int i) {
        byte[] bArr = new byte[i];
        e.nextBytes(bArr);
        return bArr;
    }
    public static char[] a(Context context, String str, char[] cArr) {
//        String packageName = context.getPackageName();
        //com.yeecall.appyeecall.sp
        String packageName="com.yeecall.app";
        char[] cArr2 = new char[((packageName.length() + cArr.length) + str.length())];
        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
        System.arraycopy(packageName.toCharArray(), 0, cArr2, cArr.length, packageName.length());
        System.arraycopy(str.toCharArray(), 0, cArr2, cArr.length + packageName.length(), str.length());
        return cArr2;
    }


}
