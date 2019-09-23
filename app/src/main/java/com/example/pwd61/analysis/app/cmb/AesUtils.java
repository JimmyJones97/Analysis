package com.example.pwd61.analysis.app.cmb;

import android.text.TextUtils;
import android.util.Base64;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.example.pwd61.analysis.Utils.utils.Logd;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:AesUtils
 * Created by pwd61 on 9/9/2019 1:56 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class AesUtils {

    /*
     * 加密
     */
    public static String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt("1IkabuKwCgzsjIuW0000000000000000", cleartext);
            Logd("orignal:"+new String(result));
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(String password, String clear) throws Exception {
        Logd(new String(password.getBytes()));
        // 创建AES秘钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES");


        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        // 加密
        return cipher.doFinal(clear.getBytes("UTF-8"));
    }

    private byte[] decrypt(byte[] content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }
}
