package com.example.pwd61.analysis.app.yeecall;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:CipherBase
 * Created by pwd61 on 2019/7/15 12:03
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class CipherBase {
    protected SecretKey a;
    protected Cipher b;
    protected Cipher c;

    /* Access modifiers changed, original: protected */
    public void initCipher(SecretKey secretKey, Cipher cipher, Cipher cipher2) {
        this.a = secretKey;
        this.b = cipher;
        this.c = cipher2;
    }

    protected static Cipher a(String str) {
        try {
            return Cipher.getInstance(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public byte[] a(byte[] bArr, byte[] bArr2) {
        Throwable th;
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        byte[] bArr3 = null;
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
            synchronized (this.b) {
                try {
                    this.b.init(1, this.a, ivParameterSpec);
                    bArr = this.b.doFinal(bArr);
                    try {
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        bArr3 = bArr;
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bArr = bArr3;
        }
        return bArr;
    }

    public byte[] b(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
            synchronized (this.c) {
                this.c.init(2, this.a, ivParameterSpec);
                bArr = this.c.doFinal(bArr);
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
