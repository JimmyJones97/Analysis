package com.example.pwd61.analysis.app.yeecall;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:StorageCipher
 * Created by pwd61 on 2019/7/16 9:02
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class StorageCipher extends CipherBase {

    public StorageCipher(SecretKey secretKey) {
        this.a = secretKey;
    }

    public boolean a() {
        Cipher a = CipherBase.a("AES/CBC/PKCS5Padding");
        Cipher a2 = CipherBase.a("AES/CBC/PKCS5Padding");
        if (a == null || a2 == null) {
            return false;
        }
        initCipher(this.a, a, a2);
        return true;
    }

}
