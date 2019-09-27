package com.example.pwd61.analysis.app.cmb;

import cmb.pb.shield.whitebox.EncryptUtil;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:BaseFunc
 * Created by pwd61 on 9/23/2019 5:31 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class BaseFunc {
    private static final char[] a = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] b = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] c = new byte[128];
    private static final char[] d = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '-'};
    private static final byte[] e = new byte[128];
//    private static java.lang.String f = com.pb.infrastructrue.g.b.class.getName();

    static {
        int i;
//        cmb.shield.InstallDex.stub();
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



    public static java.lang.String hex(byte[] bArr) {
        int i = 0;
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        java.lang.String[] strArr = new java.lang.String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        java.lang.StringBuffer stringBuffer = new java.lang.StringBuffer(bArr.length * 2);
        while (i < bArr.length) {
            stringBuffer.append(strArr[(byte) ((bArr[i] >> 4) & 15)]);
            stringBuffer.append(strArr[(byte) (bArr[i] & 15)]);
            i++;
        }
        return stringBuffer.toString();
    }

    static int a(char c) {
        if (c >= 'A') {
            return (c - 65) + 10;
        }
        return c - 48;
    }

    public static byte[] a(java.lang.String str) {
        int i = 0;
        int length = str.length();
        if (length % 2 != 0) {
            str = str + "0";
        }
        byte[] bArr = new byte[(length / 2)];
        int i2 = 0;
        do {
            int i3 = i2 + 1;
            char charAt = str.charAt(i2);
            i2 = i3 + 1;
            int a = a(charAt) * 16;
            bArr[i] = (byte) (a(str.charAt(i3)) + a);
            i++;
        } while (i2 < length);
        return bArr;
    }

    public static boolean b(java.lang.String str) {
        int length = str.length();
        int i = 0;
        while (i < length) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            }
            i++;
        }
        return true;
    }

    private static int a(char[] cArr, byte[] bArr, int i) {
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
                throw new java.lang.RuntimeException("Internal Errror");
        }
    }

    public static byte[] c(java.lang.String str) {
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
                    i += a(cArr, bArr, i);
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
        java.lang.System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    public static java.lang.String b(byte[] bArr) {
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
            cArr[length] = b[i4 >> 18];
            length = i + 1;
            cArr[i] = b[(i4 >> 12) & 63];
            int i5 = length + 1;
            cArr[length] = b[(i4 >> 6) & 63];
            i = i5 + 1;
            cArr[i5] = b[i4 & 63];
            i3 += 3;
            i2 -= 3;
            length = i;
        }
        if (i2 == 1) {
            i2 = bArr[i3] & 255;
            i = length + 1;
            cArr[length] = b[i2 >> 2];
            length = i + 1;
            cArr[i] = b[(i2 << 4) & 63];
            i2 = length + 1;
            cArr[length] = '=';
            length = i2 + 1;
            cArr[i2] = '=';
        } else if (i2 == 2) {
            i2 = ((bArr[i3] & 255) << 8) + (bArr[i3 + 1] & 255);
            i = length + 1;
            cArr[length] = b[i2 >> 10];
            length = i + 1;
            cArr[i] = b[(i2 >> 4) & 63];
            i = length + 1;
            cArr[length] = b[(i2 << 2) & 63];
            length = i + 1;
            cArr[i] = '=';
        }
        return new java.lang.String(cArr, 0, length);
    }

    public static java.lang.String a(java.lang.String str, java.lang.String str2) {
        java.lang.StringBuffer stringBuffer = new java.lang.StringBuffer();
        stringBuffer.append("<");
        stringBuffer.append(str);
        stringBuffer.append(">");
        stringBuffer.append(str2);
        stringBuffer.append("</");
        stringBuffer.append(str);
        stringBuffer.append(">");
        return stringBuffer.toString();
    }

    public static java.lang.String c(byte[] bArr) {
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
        return new java.lang.String(cArr, 0, length);
    }

    public static java.lang.String b(java.lang.String str, java.lang.String str2) {
        java.lang.String str3 = "<" + str2 + ">";
        java.lang.String str4 = "</" + str2 + ">";
        int length = str3.length();
        int indexOf = str.indexOf(str3);
        if (indexOf < 0) {
            return "";
        }
        indexOf += length;
        int indexOf2 = str.indexOf(str4, indexOf);
        if (indexOf2 < 0) {
            return "";
        }
        return str.substring(indexOf, indexOf2);
    }

    public static java.util.List<java.lang.String> c(java.lang.String str, java.lang.String str2) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.lang.String str3 = "<" + str2 + ">";
        java.lang.String str4 = "</" + str2 + ">";
        int length = str3.length();
        int length2 = str4.length();
        int i = 0;
        while (true) {
            i = str.indexOf(str3, i);
            if (i >= 0) {
                i += length;
                int indexOf = str.indexOf(str4, i);
                if (indexOf < 0) {
                    break;
                }
                arrayList.add(str.substring(i, indexOf));
                i = indexOf + length2;
            } else {
                break;
            }
        }
        return arrayList;
    }

    public static boolean d(java.lang.String str) {
        return str == null || str.length() <= 0;
    }

    public static int d(java.lang.String str, java.lang.String str2) {
        int i = 0;
        if (str == null || str2 == null || str.length() != str2.length()) {
            return -1;
        }
        if (str.equalsIgnoreCase(str2)) {
            return 0;
        }
        java.lang.String[] split = str.split("\\.");
        java.lang.String[] split2 = str2.split("\\.");
        if (split.length < split2.length) {
            return -1;
        }
        if (split.length > split2.length) {
            return 1;
        }
        while (i < split.length) {
            try {
                int parseInt = java.lang.Integer.parseInt(split[i]);
                int parseInt2 = java.lang.Integer.parseInt(split2[i]);
                if (parseInt > parseInt2) {
                    return 1;
                }
                if (parseInt < parseInt2) {
                    return -1;
                }
                i++;
            } catch (java.lang.Exception e) {
//                b(f, e.getMessage(), e);
                return -1;
            }
        }
        return -1;
    }

    public static int e(java.lang.String str, java.lang.String str2) {
        if (str2 == null) {
            str2 = "null";
        }
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.d(str, "" + str2);
    }

    public static int a(java.lang.String str, java.lang.String str2, java.lang.Throwable th) {
        if (str2 == null) {
            str2 = "null";
        }
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.d(str, "" + str2, th);
    }

    public static int f(java.lang.String str, java.lang.String str2) {
        if (str2 == null) {
            str2 = "null";
        }
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.v(str, "" + str2);
    }

    public static int b(java.lang.String str, java.lang.String str2, java.lang.Throwable th) {
        if (str2 == null) {
            str2 = "null";
        }
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.v(str, "" + str2);
    }

    public static int a(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        if (android.text.TextUtils.isEmpty(str2)) {
            str2 = "null";
        }
        if (android.text.TextUtils.isEmpty(str3)) {
            str3 = "null";
        }
        java.lang.String str4 = str2 + "|" + str3;
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.v(str, str4);
    }

    public static int g(java.lang.String str, java.lang.String str2) {
        if (str2 == null) {
            str2 = "null";
        }
//        if (com.pb.infrastructrue.g.j.c) {
//            return 0;
//        }
        return android.util.Log.v(str, str2);
    }

    public static java.lang.String e(java.lang.String str) {
        java.lang.String str2 = null;
        try {
            byte[] bytes = str.getBytes();
            java.security.MessageDigest instance = java.security.MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bytes);
            return a(instance.digest(), "");
        } catch (java.security.NoSuchAlgorithmException e) {
//            b(f, e.getMessage(), e);
            return str2;
        } catch (java.lang.Exception e2) {
//            b(f, e2.getMessage(), e2);
            return str2;
        }
    }

    public static java.lang.String a(byte[] bArr, java.lang.String str) {
        java.lang.StringBuffer stringBuffer = new java.lang.StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(java.lang.Integer.toHexString((b & 240) >>> 4));
            stringBuffer.append(java.lang.Integer.toHexString(b & 15)).append(str);
        }
        return stringBuffer.toString();
    }

    public static byte[] d(byte[] bArr) {
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        java.lang.String str = "fileprotect";
        int charAt = (byte) str.charAt(0);
        int i = 1;
        int i2 = charAt;
        while (i < str.length()) {
            byte charAt2 = (byte) str.charAt(i);
            i++;
            byte b = charAt2;
            charAt2 = (byte) (charAt ^ charAt2);
            byte charAt3 = b;
        }
        for (i = 0; i < length; i++) {
            bArr2[i] = (byte) (bArr[i] ^ i2);
        }
        return bArr2;
    }

    public static byte[] a(byte[] bArr, java.lang.StringBuffer stringBuffer) {
        int i = 0;
        if (bArr == null) {
            stringBuffer.append("内容不能为空。#-1");
            return null;
        }
        byte[] d = d(bArr);
        int length = d.length;
        int i2 = 0;
        for (byte b : d) {
            i2 += ((char) b) & 255;
        }
        byte[] bArr2 = new byte[]{(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255)};
        byte[] bArr3 = new byte[(length + 4)];
        for (i2 = 0; i2 < 4; i2++) {
            bArr3[i2] = bArr2[i2];
        }
        while (i < length) {
            bArr3[i + 4] = d[i];
            i++;
        }
        return bArr3;
    }

    public static byte[] b(byte[] bArr, java.lang.StringBuffer stringBuffer) {
        int i = 4;
        if (bArr == null) {
            stringBuffer.append("内容长度为空");
            return null;
        }
        int length = bArr.length;
        if (length <= 0) {
            stringBuffer.append("内容长度为空");
            return null;
        } else if (length <= 4) {
            stringBuffer.append("内容长度不符合招行标准");
            return null;
        } else {
            byte[] bArr2 = new byte[(length - 4)];
            int i2 = 0;
            while (i < length) {
                i2 += ((char) bArr[i]) & 255;
                bArr2[i - 4] = bArr[i];
                i++;
            }
            if (((((bArr[0] << 24) | ((bArr[1] << 16) & 16777215)) | ((bArr[2] << 8) & 65535)) | (bArr[3] & 255)) == i2) {
                return d(bArr2);
            }
            stringBuffer.append("内容校验不通过");
            return null;
        }
    }

    public static byte[] h(java.lang.String str, java.lang.String str2) {
        byte[] bArr = null;
        if (str == null || str2 == null) {
            return bArr;
        }
        byte[] bytes = str.getBytes();
        if (bytes == null) {
            return bArr;
        }
        try {
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(str2.getBytes(), "DES");
            javax.crypto.Cipher instance = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
            instance.init(1, secretKeySpec);
            return instance.doFinal(bytes);
        } catch (java.lang.Exception e) {
//            b(f, e.getMessage(), e);
            return bArr;
        }
    }

    public static java.lang.String a() {
        try {
//            return com.pb.infrastructrue.a.a.a().b().e().getPackageManager().getPackageInfo(com.pb.infrastructrue.a.a.a().b().e().getPackageName(), 64).signatures[0].toCharsString();
            return "";
        } catch (java.lang.Exception e) {
//            if (!com.pb.infrastructrue.g.j.c) {
//                b(f, e.getMessage(), e);
//            }
            return "";
        }
    }

    public static java.lang.String b() {
        java.lang.String str = "";
        try {
            str = android.os.Build.VERSION.RELEASE;
        } catch (java.lang.Exception e) {
//            b(f, e.getMessage(), e);
            str = "";
        }
        if (str == null) {
            return "";
        }
        return str;
    }

    public static byte[] f(java.lang.String str) throws java.security.NoSuchAlgorithmException, java.io.UnsupportedEncodingException, java.security.InvalidKeyException {
        javax.crypto.Mac instance = javax.crypto.Mac.getInstance("HmacSHA1");
        instance.init(new javax.crypto.spec.SecretKeySpec(EncryptUtil.decryptcbc("K3WIX19CZEQ2IDhSjmUClQ==").getBytes("UTF-8"), instance.getAlgorithm()));
        return instance.doFinal(str.getBytes());
    }

    public static int a(android.content.Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    public static byte[] e(byte[] bArr) throws java.lang.Exception {
        if (bArr == null) {
            return null;
        }
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        java.util.zip.GZIPOutputStream gZIPOutputStream = new java.util.zip.GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(bArr);
        gZIPOutputStream.finish();
        gZIPOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static java.lang.String a(long j) {
        float f;
        java.lang.String str;
        float f2 = (float) j;
        java.lang.String str2 = "B";
        if (f2 > 900.0f) {
            str2 = "KB";
            f2 /= 1024.0f;
        }
        if (f2 > 900.0f) {
            str2 = "MB";
            f2 /= 1024.0f;
        }
        if (f2 > 900.0f) {
            str2 = "GB";
            f2 /= 1024.0f;
        }
        if (f2 > 900.0f) {
            str2 = "TB";
            f2 /= 1024.0f;
        }
        if (f2 > 900.0f) {
            f = f2 / 1024.0f;
            str = "PB";
        } else {
            f = f2;
            str = str2;
        }
        if (f < 1.0f) {
            str2 = "%.2f";
        } else if (f < 10.0f) {
            str2 = "%.2f";
        } else if (f < 100.0f) {
            str2 = "%.2f";
        } else {
            str2 = "%.0f";
        }
        return java.lang.String.format(str2, new java.lang.Object[]{java.lang.Float.valueOf(f)}) + " " + str;
    }

    public static java.lang.String i(java.lang.String str, java.lang.String str2) {
        if (android.text.TextUtils.isEmpty(str) || android.text.TextUtils.isEmpty(str2)) {
            return "";
        }
        java.util.regex.Pattern compile = java.util.regex.Pattern.compile(str2);
        if (compile == null) {
            return "";
        }
        java.util.regex.Matcher matcher = compile.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
