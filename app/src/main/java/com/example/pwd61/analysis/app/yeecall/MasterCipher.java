package com.example.pwd61.analysis.app.yeecall;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.pwd61.analysis.Utils.FileUtils;
import com.yeecall.library.securestorage.xor.XorJNI;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.util.UUID;

import static com.example.pwd61.analysis.Utils.Utils.Logd;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:MasterCipher
 * Created by pwd61 on 2019/7/15 12:04
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class MasterCipher extends CipherBase {
    private Context ctx;
    private char[] e;
    private final KeyValueDao databaseDao;
    private String stringset;

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }


    public MasterCipher(Context context, String str, KeyValueDao dao, char[] cArr) {
        this.ctx = context;
        this.stringset = str;
        Logd("MasterCipher: " + this.stringset+", cArr:"+new String(cArr));
        this.e = cArr;
        this.databaseDao = dao;
    }

    /**
     * first check database;
     *
     * @return return is ok database?
     */
    public boolean firstCheck() {
        PackageInfo packageInfo;
        byte[] digest = null;
        byte[] a;
        byte[] a2;
        byte[] bArr;
        int length;
        char[] cArr;
        SecretKey secretKey;
        String stringBuilder = this.stringset + "tb295d117135a9763da282e7dae73a5ca7d3e5b11";
        String firstEnc = CipherProtocol.a(stringBuilder);
        String stringBuilder2 = this.stringset + "t3fa6658ccb0eaaaaba8d3e5cf002c0e2847a736b";
        String secEnc = CipherProtocol.a(stringBuilder2);
        try {
            packageInfo = this.ctx.getPackageManager().getPackageInfo(this.ctx.getPackageName(), PackageManager.GET_SIGNATURES);//64
        } catch (Exception unused) {
            packageInfo = null;
            unused.printStackTrace();
            return false;
        }
        if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures[0] == null)) {
            try {
                MessageDigest instance = MessageDigest.getInstance("SHA1");
                // read signature from target phone.
//                FileUtils.readFileFromSDCard("/sdcard/signature");
                //instance.update(packageInfo.signatures[0].toByteArray());//signature as seed.
                instance.update(FileUtils.readFileFromSDCard("/sdcard/signature"));
                digest = instance.digest();
            } catch (Throwable unused2) {
                packageInfo = null;
                unused2.printStackTrace();
                return false;
            }
            if (digest == null || digest.length == 0) {
                digest = "7d958a25aeb1e95c5ab39d4df54ab56e".getBytes();
            }
            a = this.databaseDao.getData(firstEnc);/// read data from  database,if error get a new one.
            if (a == null) {
                Logd("数据为空");
                byte[] a6 = CipherProtocol.a(128);
                if (this.databaseDao.a(firstEnc, a6) <= 0) {// write to database..
                    Logd("sssss");
                    return false;
                }
                StringBuilder stringBuilder3;
                String stringBuilder4 = null;
                firstEnc = UUID.randomUUID().toString();
                TelephonyManager telephonyManager = (TelephonyManager) this.ctx.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    PackageManager pm = this.ctx.getPackageManager();
                    if (PackageManager.PERMISSION_GRANTED == this.ctx.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)) {
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(telephonyManager.getDeviceId());
                        stringBuilder3.append("|");
                        stringBuilder3.append(telephonyManager.getSubscriberId());
                        stringBuilder4 = stringBuilder3.toString();
                        Logd("pm: "+stringBuilder4);
                    }
                } catch (Throwable unused3) {
                    stringBuilder4 = "t9cf7b6936fd3318dbec225d6b56208878e56a002";
                }
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(firstEnc);
                stringBuilder3.append(stringBuilder4);
                firstEnc = stringBuilder3.toString();
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(this.stringset);
                stringBuilder5.append(firstEnc);
                a2 = HexUtils.a(CipherProtocol.a(stringBuilder5.toString()).substring(1));
                XorJNI.xor(a2, 0, a2.length, a6, 0, a6.length);
                bArr = a6;
                XorJNI.xor(a2, 0, a2.length, digest, 0, digest.length);
                if (this.databaseDao.a(secEnc, a2) <= 0) {
                    return false;
                }
            }
            bArr = a;
            a2 = this.databaseDao.getData(secEnc);
            if (a2 != null && a2.length > 0) {
//                Logd("数据不为空:"+ HexUtils.byteArr2Str(a2)+);
                a = a2;
                Logd("数据不为空,a2:"+ HexUtils.byteArr2Str(a2)+",a:"+HexUtils.byteArr2Str(a)+",bArr:"+HexUtils.byteArr2Str(bArr));
                Logd("数据不为空,digest:"+HexUtils.byteArr2Str(digest));
                Logd("长度：a2:"+a2.length+",digest:"+digest.length);
//11
                XorJNI.xor(a, 0, a2.length, digest, 0, digest.length);

                Logd("firstCheck1: a:"+HexUtils.byteArr2Str(a)+",digest:"+HexUtils.byteArr2Str(digest)+",a2:"+HexUtils.byteArr2Str(a2));
                Logd("长度2：a2:"+a2.length+",bArr:"+bArr.length);
                //2
                XorJNI.xor(a, 0, a2.length, bArr, 0, bArr.length);
                Logd("firstCheck2: a:"+HexUtils.byteArr2Str(a)+",bArr:"+HexUtils.byteArr2Str(bArr)+",a2:"+HexUtils.byteArr2Str(a2));
                firstEnc = HexUtils.byteArr2Str(a2);
                length = firstEnc.length();
                cArr = new char[(this.e.length + length)];//80
                System.arraycopy(firstEnc.toCharArray(), 0, cArr, 0, length);
                System.arraycopy(this.e, 0, cArr, length, this.e.length);
                EncodeUtils.a(this.e);
                this.e = cArr;
            }
            Logd("e:"+new String(this.e)+",arr:"+ HexUtils.byteArr2Str(bArr));
            secretKey = a(this.e, bArr);//
            if (secretKey == null) {
                Logd("error in here ?");
                return false;
            }
            Cipher cipher = CipherBase.a("AES/CBC/PKCS5Padding");
            Cipher cipher1 = CipherBase.a("AES/CBC/PKCS5Padding");
            if (cipher == null || cipher1 == null) {
                Logd("cipher noll");
                return false;
            }
            initCipher(secretKey, cipher, cipher1);
            EncodeUtils.a(this.e);
            this.e = null;
            return true;

        } else {
            Logd("failed fc");
            return false;
        }
    }

    private static SecretKey a(char[] cArr, byte[] bArr) {
        try {
            return new SecretKeySpec(SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC").generateSecret(new PBEKeySpec(cArr, bArr, 37, 128)).getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public StorageCipher b(String str) {
        SecretKey c;
        SecureThread.a();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("ta727348c8aa7823aa5f18dc02a066498bfd8b132");
        String a = CipherProtocol.a(stringBuilder.toString());
        byte[] a2 = this.databaseDao.getData(a);
        byte[] a3;
        if (a2 == null) {
            c = c();
            if (c == null) {
                return null;
            }
            byte[] encoded = c.getEncoded();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("ta727348c8aa7823aa5f18dc02a066498bfd8b132");
            a3 = a(encoded, CipherProtocol.a(stringBuilder2.toString(), 16));
            if (a3 == null || this.databaseDao.a(a, a3) <= 0) {
                return null;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("ta727348c8aa7823aa5f18dc02a066498bfd8b132");
        a3 = b(a2, CipherProtocol.a(stringBuilder.toString(), 16));
        if (a3 == null) {
            return null;
        }
        c = a(a3);
        if (c == null) {
            return null;
        }
        StorageCipher heo = new StorageCipher(c);
        if (heo.a()) {
            return heo;
        }
        return null;
    }

    private SecretKey c() {
        try {
            KeyGenerator instance = KeyGenerator.getInstance("AES");
            instance.init(128);
            return instance.generateKey();
        } catch (Exception unused) {
            return null;
        }
    }

    private SecretKey a(byte[] bArr) {
        return new SecretKeySpec(bArr, "AES");
    }

    public boolean b() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.stringset);
        stringBuilder.append("t26a26ebfab9b4e5f9f39784402706fd6efdf7081");
        String stringBuilder2 = stringBuilder.toString();
        String a = CipherProtocol.a(stringBuilder2);
        byte[] a2 = this.databaseDao.getData(a);
        boolean z = false;
        if ((a2 == null ? 1 : null) != null) {
            if (this.databaseDao.a(a, a(TypeBytes.a(1), CipherProtocol.a(stringBuilder2, 16))) > 0) {
                z = true;
            }
            return z;
        }
        byte[] b = b(a2, CipherProtocol.a(stringBuilder2, 16));
        if (b == null) {
            return false;
        }
        if (TypeBytes.a(b) == 1) {
            z = true;
        }
        return z;
    }

}
