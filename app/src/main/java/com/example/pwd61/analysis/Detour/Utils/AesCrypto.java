package com.example.pwd61.analysis.Detour.Utils;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:AesCrypto
 * Created by pwd61 on 2019/5/30 9:57
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class AesCrypto {
    private static byte[] md = null;

    public static String encrypt(String str, String str2) {
        try {
            return toHex(encrypt(fD(str), str2.getBytes()));
        } catch (Throwable th) {
            return null;
        }
    }

    public static String decrypt(String str, String str2) {
        try {
            return new String(decrypt(fD(str), toByte(str2)));
        } catch (Throwable th) {
            return "";
        }
    }

    private static byte[] fD(String str) throws Exception {
        if (md != null) {
            return md;
        }
        md = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(str.toCharArray(), getSalt(), 10, 128)).getEncoded(), "AES").getEncoded();
        return md;
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance("AES");
        instance.init(1, secretKeySpec);
        return instance.doFinal(bArr2);
    }

    private static byte[] decrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance("AES");
        instance.init(2, secretKeySpec);
        return instance.doFinal(bArr2);
    }

    public static byte[] toByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = Integer.valueOf(str.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return bArr;
    }

    public static String toHex(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte appendHex : bArr) {
            appendHex(stringBuffer, appendHex);
        }
        return stringBuffer.toString();
    }

    private static void appendHex(StringBuffer stringBuffer, byte b) {
        stringBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }

    static private byte[] getSalt() throws IOException {
        byte[] bArr = new byte[16];
        String salt = "!q@w#e$r%t^y#n@v";
        bArr = salt.getBytes();
//        byte[] bytes = "!q@w".getBytes();
//        byte[] bytes2 = JdSdk.getInstance().getApplicationContext().getResources().getString(R.string.privateKeyP2).getBytes();
//        byte[] decode = Base64.decode("JXReeQ==");
//        byte[] bArr2 = new byte[bytes.length];
//        for (int i = 0; i < bytes.length; i++) {
//            bArr2[i] = (byte) (((bytes[i] + bytes2[i]) + decode[i]) / 3);
//        }
//        System.arraycopy(bytes, 0, bArr, 0, 4);
//        System.arraycopy(bytes2, 0, bArr, 4, 4);
//        System.arraycopy(decode, 0, bArr, 8, 4);
//        System.arraycopy(bArr2, 0, bArr, 12, 4);
        return bArr;
    }


}
