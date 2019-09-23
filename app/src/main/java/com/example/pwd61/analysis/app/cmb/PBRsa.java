package com.example.pwd61.analysis.app.cmb;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:PBRsa
 * Created by pwd61 on 9/9/2019 11:01 AM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.example.pwd61.analysis.Utils.utils.Logd;

public class PBRsa {
    static byte[] a = new byte[]{(byte) -76, (byte) -119, (byte) -96, (byte) -102, (byte) -74, (byte) 45, (byte) 88, (byte) 88, (byte) -108, (byte) -9, (byte) -26, (byte) -14, (byte) -7, (byte) 70, (byte) 83, (byte) 67, (byte) 116, (byte) 4, (byte) 29, (byte) 47, (byte) -12, (byte) -88, (byte) -43, (byte) 112, (byte) -83, (byte) -119, (byte) 109, (byte) 34, (byte) -68, (byte) 3, (byte) -117, (byte) 87, (byte) -14, (byte) -57, (byte) 25, (byte) 93, (byte) 18, (byte) -71, (byte) 12, (byte) -42, (byte) -21, (byte) -99, (byte) 92, (byte) 63, (byte) -75, (byte) -19, (byte) 76, (byte) -57, (byte) -102, (byte) 7, (byte) -27, (byte) 80, (byte) 66, (byte) 73, (byte) 23, (byte) -8, (byte) 69, (byte) -2, (byte) 113, (byte) 102, (byte) 46, (byte) -110, (byte) 65, (byte) 121};
    private static char[] b = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] d = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '-'};
    private static final byte[] c = new byte[128];
    private static final byte[] e = new byte[128];

    static {
        int i;
//        InstallDex.stub();
        int i2 = 0;
        for (i = 0; i < c.length; i++) {
            c[i] = Byte.MAX_VALUE;
        }
        for (i = 0; i < b.length; i++) {
            c[b[i]] = (byte) i;
        }
        for (i = 0; i < e.length; i++) {
            e[i] = Byte.MAX_VALUE;
        }
        while (i2 < d.length) {
            e[d[i2]] = (byte) i2;
            i2++;
        }
    }


    public static byte[] a(byte[] bArr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (bArr == null) {
            return null;
        }
        PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(1, a), new BigInteger(1, new byte[]{(byte) 1, (byte) 0, (byte) 1})));
        Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        instance.init(1, generatePublic);
        return instance.doFinal(bArr);
    }

    public static String cnv(byte[] bArr) {
        int length = bArr.length;
        if (length <= 0) {
            return "";
        }
        int i;
        char[] cArr = new char[(((length / 3) * 4) + 4)];
        int i2 = length - 0;
        int i3 = 0;
        length = 0;
        while (i2 >= 3) {
            int i4 = (bArr[i3 + 2] & 255) + (((bArr[i3] & 255) << 16) + ((bArr[i3 + 1] & 255) << 8));
            i = length + 1;
            cArr[length] = d[i4 >> 18];
            length = i + 1;
            cArr[i] = d[(i4 >> 12) & 63];
            int i5 = length + 1;
            cArr[length] = d[(i4 >> 6) & 63];
            i = i5 + 1;
            cArr[i5] = d[i4 & 63];
            i3 += 3;
            i2 -= 3;
            length = i;
        }
        if (i2 == 1) {
            i2 = bArr[i3] & 255;
            i = length + 1;
            cArr[length] = d[i2 >> 2];
            length = i + 1;
            cArr[i] = d[(i2 << 4) & 63];
            i2 = length + 1;
            cArr[length] = '_';
            length = i2 + 1;
            cArr[i2] = '_';
        } else if (i2 == 2) {
            i2 = ((bArr[i3] & 255) << 8) + (bArr[i3 + 1] & 255);
            i = length + 1;
            cArr[length] = d[i2 >> 10];
            length = i + 1;
            cArr[i] = d[(i2 >> 4) & 63];
            i = length + 1;
            cArr[length] = d[(i2 << 2) & 63];
            length = i + 1;
            cArr[i] = '_';
        }
        return new String(cArr, 0, length);
    }

    public static String a(String str) {
        String a;
        InvalidKeyException e;
        NoSuchAlgorithmException e2;
        InvalidKeySpecException e3;
        NoSuchPaddingException e4;
        IllegalBlockSizeException e5;
        BadPaddingException e6;
        if (str == null) {
            return null;
        }
        byte[] bytes;
        a = "11111";
        String str2 = "AAA" + a.substring(a.length() - 5) + str + "";
        try {
            bytes = str2.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e7) {
            Logd("PBRsa" + e7.getMessage());
            bytes = str2.getBytes();
        }
        Logd("PBRsa" ,"strPLAINTXT:" + str2);
        try {
            a = cnv(a(bytes));

            Logd("PBRsa" , "strEncryptedText:" + a);

        } catch (InvalidKeyException e14) {
            InvalidKeyException invalidKeyException = e14;
            a = null;
            e = invalidKeyException;
        } catch (NoSuchAlgorithmException e15) {
            NoSuchAlgorithmException noSuchAlgorithmException = e15;
            a = null;
            e2 = noSuchAlgorithmException;
            Logd("PBRsa", e2.getMessage(), e2);
            return a;
        } catch (InvalidKeySpecException e16) {
            InvalidKeySpecException invalidKeySpecException = e16;
            a = null;
            e3 = invalidKeySpecException;
            Logd("PBRsa", e3.getMessage(), e3);
            return a;
        } catch (NoSuchPaddingException e17) {
            NoSuchPaddingException noSuchPaddingException = e17;
            a = null;
            e4 = noSuchPaddingException;
            Logd("PBRsa", e4.getMessage(), e4);
            return a;
        } catch (IllegalBlockSizeException e18) {
            IllegalBlockSizeException illegalBlockSizeException = e18;
            a = null;
            e5 = illegalBlockSizeException;
            Logd("PBRsa", e5.getMessage(), e5);
            return a;
        } catch (BadPaddingException e19) {
            BadPaddingException badPaddingException = e19;
            a = null;
            e6 = badPaddingException;
            Logd("PBRsa", e6.getMessage(), e6);
            return a;
        }

        return a;
    }

    public static String a(String str, String str2) {
        String c;
        InvalidKeyException e;
        NoSuchAlgorithmException e2;
        InvalidKeySpecException e3;
        NoSuchPaddingException e4;
        IllegalBlockSizeException e5;
        BadPaddingException e6;
        if (str == null) {
            return null;
        }
        byte[] bytes;
        if (str2 == null || str2.length() < 5) {
            str2 = "11111";
        }
        String str3 = "AAA" + str2.substring(str2.length() - 5) + str + "";
        try {
            bytes = str3.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e7) {
            Logd("PBRsa", e7.getMessage(), e7);
            bytes = str3.getBytes();
        }
        Logd("PBRsa", "strPLAINTXT:" + str3);
        try {
            c = cnv(a(bytes));

        } catch (InvalidKeyException e14) {
            InvalidKeyException invalidKeyException = e14;
            c = null;
            e = invalidKeyException;
        } catch (NoSuchAlgorithmException e15) {
            NoSuchAlgorithmException noSuchAlgorithmException = e15;
            c = null;
            e2 = noSuchAlgorithmException;
            Logd("PBRsa", e2.getMessage(), e2);
            return c;
        } catch (InvalidKeySpecException e16) {
            InvalidKeySpecException invalidKeySpecException = e16;
            c = null;
            e3 = invalidKeySpecException;
            Logd("PBRsa", e3.getMessage(), e3);
            return c;
        } catch (NoSuchPaddingException e17) {
            NoSuchPaddingException noSuchPaddingException = e17;
            c = null;
            e4 = noSuchPaddingException;
            Logd("PBRsa", e4.getMessage(), e4);
            return c;
        } catch (IllegalBlockSizeException e18) {
            IllegalBlockSizeException illegalBlockSizeException = e18;
            c = null;
            e5 = illegalBlockSizeException;
            Logd("PBRsa", e5.getMessage(), e5);
            return c;
        } catch (BadPaddingException e19) {
            BadPaddingException badPaddingException = e19;
            c = null;
            e6 = badPaddingException;
            Logd("PBRsa", e6.getMessage(), e6);
            return c;
        }
//        Logd("PBRsa", e.getMessage(), e);
        return c;
    }

    private static int ctoba(char[] cArr, byte[] bArr, int i) {
        int i2;
        if (cArr[3] == '=') {
            i2 = 2;
        } else {
            i2 = 3;
        }
        if (cArr[2] == '=') {
            i2 = 1;
        }
        byte b = c[cArr[0]];
        byte b2 = c[cArr[1]];
        byte b3 = c[cArr[2]];
        byte b4 = c[cArr[3]];
        switch (i2) {
            case 1:
                bArr[i] = (byte) (((b << 2) & 252) | ((b2 >> 4) & 3));
                return 1;
            case 2:
                i2 = i + 1;
                bArr[i] = (byte) (((b << 2) & 252) | ((b2 >> 4) & 3));
                bArr[i2] = (byte) (((b2 << 4) & 240) | ((b3 >> 2) & 15));
                return 2;
            case 3:
                i2 = i + 1;
                bArr[i] = (byte) (((b << 2) & 252) | ((b2 >> 4) & 3));
                int i3 = i2 + 1;
                bArr[i2] = (byte) (((b2 << 4) & 240) | ((b3 >> 2) & 15));
                bArr[i3] = (byte) (((b3 << 6) & 192) | (b4 & 63));
                return 3;
            default:
                throw new RuntimeException("Internal Errror");
        }
    }

    public static byte[] ctob(String str) {
        char[] cArr = new char[4];
        byte[] bArr = new byte[(((str.length() / 4) * 3) + 3)];
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            char charAt = str.charAt(i3);
            if (charAt == '=' || (charAt < c.length && c[charAt] != Byte.MAX_VALUE)) {
                int i4 = i2 + 1;
                cArr[i2] = charAt;
                if (i4 == cArr.length) {
                    i += ctoba(cArr, bArr, i);
                    i2 = 0;
                } else {
                    i2 = i4;
                }
            }
        }
        if (i == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    public static boolean b(String str, String str2) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return z;
        }
        try {
            byte[] c = ctob(str2);
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(1, a), new BigInteger(1, new byte[]{(byte) 1, (byte) 0, (byte) 1})));
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initVerify(generatePublic);
            instance.update(str.getBytes("UTF-8"));
            return instance.verify(c);
        } catch (Exception e) {
            Logd("PBRsa", e.getMessage());
            return z;
        }
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static String b(String str) throws Exception {
        int i = 0;
        byte[] bArr = new byte[]{(byte) 1, (byte) 0, (byte) 1};
        BigInteger bigInteger = new BigInteger(1, a);
        Cipher.getInstance("RSA").init(1, KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(bigInteger, new BigInteger(1, bArr))));
        int bitLength = bigInteger.bitLength() / 8;
        Logd("PBRsa", "encryptByPublicKey,key_len=" + bitLength);
        byte[][] a = a(str, bitLength - 11);
        String str2 = "";
        bArr = new byte[0];
        int length = a.length;
        while (i < length) {
            bArr = a(bArr, a(a[i]));
            i++;
        }
        return cnv(bArr);
    }

    public static boolean len(String str) {
        return str == null || str.length() <= 0;
    }

    public static byte[][] a(String str, int i) {
        if (len(str)) {
            return (byte[][]) null;
        }
        byte[] bytes;
        int i2;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logd("PBRsa", e.getMessage(), e);
            bytes = str.getBytes();
        }
        int length = bytes.length / i;
        int length2 = bytes.length % i;
        if (length2 != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        byte[][] bArr = new byte[(length + i2)][];
        for (int i3 = 0; i3 < length + i2; i3++) {
            byte[] bArr2;
            if (i3 != (length + i2) - 1 || length2 == 0) {
                bArr2 = new byte[i];
                System.arraycopy(bytes, i3 * i, bArr2, 0, i);
                bArr[i3] = bArr2;
            } else {
                bArr2 = new byte[length2];
                System.arraycopy(bytes, i3 * i, bArr2, 0, length2);
                bArr[i3] = bArr2;
            }
        }
        return bArr;
    }

}
