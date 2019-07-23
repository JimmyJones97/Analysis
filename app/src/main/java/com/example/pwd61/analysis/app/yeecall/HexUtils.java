package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:HexUtils
 * Created by pwd61 on 2019/7/12 10:29
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class HexUtils {
    static char[] a = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String byteArr2Str(byte[] bArr) {
        return bArr == null ? "(null)" : a(bArr, 0, bArr.length);
    }

    public static String a(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length < i + i2) {
            return "(null)";
        }
        char[] cArr = new char[(i2 * 2)];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i3 + i3;
            byte b = bArr[i + i3];
            cArr[i4] = a[(b >> 4) & 15];
            cArr[i4 + 1] = a[b & 15];
        }
        return new String(cArr);
    }

    public static byte[] a(String str) {
        if (str == null || str.length() == 0 || str.length() % 2 != 0) {
            throw new RuntimeException("Bad input: null pointer");
        }
        char[] toCharArray = str.toCharArray();
        if (toCharArray == null || toCharArray.length % 2 == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bad input: length == ");
            stringBuilder.append(str.length());
            throw new RuntimeException(stringBuilder.toString());
        }
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i << 1;
            int a = a(toCharArray[i2]);
            bArr[i] = (byte) ((a(toCharArray[i2 + 1]) & 255) | ((a & 255) << 4));
        }
        return bArr;
    }

    private static int a(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 97) + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 65) + 10;
        }
        throw new RuntimeException("Not a hex symbol");
    }

}
