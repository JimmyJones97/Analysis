package com.example.pwd61.analysis;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pwd61.analysis.Detour.verfiy.ByteUtil;
import com.example.pwd61.analysis.Utils.FileUtils;
import com.example.pwd61.analysis.Utils.utils;
import com.example.pwd61.analysis.app.Yeecall;
import com.example.pwd61.analysis.app.cmb.AesUtils;
import com.example.pwd61.analysis.app.cmb.PBRsa;
import com.example.pwd61.analysis.app.yeecall.CipherProtocol;
import com.example.pwd61.analysis.app.yeecall.HashUtils;
import com.android.tencent.qq.qq.Utils;
import com.example.pwd61.analysis.app.yeecall.IKeyValueStorage;
import com.example.pwd61.analysis.app.yeecall.PreferencesImpl;
import com.example.pwd61.analysis.app.yeecall.ZayhuPref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import cmb.pb.shield.whitebox.EncryptUtil;

import static com.example.pwd61.analysis.Utils.utils.Logd;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;


public class MyActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "HACK";

    Button ctf;
    Button cmb;
    ScrollView scrollView;
    LinearLayout ll_content;
    Uri uri = Uri.parse("content://hb.android.contentProvider/teacher");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = (ScrollView) findViewById(R.id.logtxt);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        Button yeecal = findViewById(R.id.yeecall);
        Button tst1 = findViewById(R.id.Test1);
        Button tst2 = findViewById(R.id.Test2);
        ctf = (Button) findViewById(R.id.ctf);
        cmb = (Button) findViewById(R.id.cmb);
        yeecal.setOnClickListener(this);
        tst2.setOnClickListener(this);
        tst1.setOnClickListener(this);
        ctf.setOnClickListener(this);
        cmb.setOnClickListener(this);


        copyAssets();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.yeecall:
                /**** 解密yc_c.sp ****/
                String data = "a834d1d61d7b2c9d7072adb704dab86010712b5b9a731ec5d2ed7ea2bd61832c4f085c51b6fcd948adde96f0067fc06d672acedc3d72f2db155455bd4cdef73e";
                String SS = HashUtils.SHA1(data);
                addit("yee", SS);
                addit("tyee", CipherProtocol.a("c_db_kvs_xxxxxslat"));

                IKeyValueStorage IKS = ZayhuPref.a(getApplicationContext(), "c_db_kvs_xxxxx", 5);


                String dbName = "yeecall.sp";
                String STR = dbName + "tcfb3352c2df335696c6bc631932c6a61a4cdf318";
                String dbNameEnc = CipherProtocol.a(STR);
                Logd("数据库名字：" + dbName + ",enc: " + dbNameEnc);

                String stringBuilder2 = dbNameEnc + "t72f283666ae9a3482660515b0f9acebeaff91e04";
                String columnsID = CipherProtocol.a(stringBuilder2);
                Logd("加密列：" + columnsID);
                Logd("test ：" + PreferencesImpl.b("1".toCharArray()));
                Logd("test1 :" + PreferencesImpl.b("1234".toCharArray()));
                Yeecall.getKey(getApplicationContext());
                HttpURLConnection con;
//                con.setRequestProperty("","");

                break;
            case R.id.Test1:
                Log.d(TAG, "测试1");
                break;
            case R.id.Test2:
                Log.d(TAG, "测试2");
                Log.d(TAG, "免密提取8.0");
                addit("Doge", jiemi(ReadUserInfo()));
                addit("doge2", jiemi("D68DBFF8A8E300A855F10E79F79777B8BA5F46DC38646BD2FE0762B9AC53007889249D691E7011503BD1688DB6D0D38FB74F25F1579E0A6584E1E878E8A0CFEF99F0C6BF55B528EAEA49A99C4D5A46DBFF483988CFBDA0D3592E238D0B84CEBAF0B83A05F07127048338E7220ECF96C6ADC7E5F468FEBB9A2C53F573F507DF1FD42C3E902718F2E3FC1208159DD75BE2DA94A2BBCBBE9C1FA603B35FF774E6A79B4C8A4DDCA54020EC65A1AAB99144D51F842BDDBF772FE0673C704BDE5710E35D41541A1CD132DB4D742DDB40F5516DACFCBF978C00F3BC39CAF3A4353DA2C1DBE7CB43DA4E0000B9277E428D40C692A5CB7739DCBED473858556146FFF92C7"));
                addit("pin", new String(ByteUtil.parseHexStr2Byte("616c696b6169353230")));
                addit("pin", new String(ByteUtil.parseHexStr2Byte("313136393436363237326c696b756e")));
                addit("unencr", new String(unencry("D68DBFF8A8E300A855F10E79F79777B8BA5F46DC38646BD2FE0762B9AC53007889249D691E7011503BD1688DB6D0D38FB74F25F1579E0A6584E1E878E8A0CFEF99F0C6BF55B528EAEA49A99C4D5A46DBFF483988CFBDA0D3592E238D0B84CEBAF0B83A05F07127048338E7220ECF96C6ADC7E5F468FEBB9A2C53F573F507DF1FD42C3E902718F2E3FC1208159DD75BE2DA94A2BBCBBE9C1FA603B35FF774E6A79B4C8A4DDCA54020EC65A1AAB99144D51F842BDDBF772FE0673C704BDE5710E35D41541A1CD132DB4D742DDB40F5516DACFCBF978C00F3BC39CAF3A4353DA2C1DBE7CB43DA4E0000B9277E428D40C692A5CB7739DCBED473858556146FFF92C7")));
                break;
            case R.id.ctf:
                Log.d(TAG, "ctf");
                addit("ctf", !Utils.cec("10864017", "1234") ? "succ" : "failed!");
                addit("[+]", EncryptUtil.decryptcbc("z/GU3NDcvTToe21svo0+KQ=="));
                break;
            case R.id.cmb:
                Log.d(TAG, "cmb");
                //utils.Logd(Utils.cec("10864017","1234")==false?"succ":"failed!");
                addit(" [+]", EncryptUtil.decryptcbc("z/GU3NDcvTToe21svo0+KQ=="));
                addit(" [+]", EncryptUtil.decryptcbc("K3WIX19CZEQ2IDhSjmUClQ=="));
                addit("shijiea ", PBRsa.a("WcXqq0aNxzVLVxKi"));
                addit("shijiea ", PBRsa.a("QhVi0qpMKXJ3P3M5"));
                addit("aes", AesUtils.encrypt("<PostData><DeviceType>E</DeviceType><Version>7.2.0</Version><SystemVersion>9</SystemVersion><ExtraFormat>PNG</ExtraFormat><AppID>0029000000020190903013227020000000000q5Tr3xiEc3AUByxNbqrru3t9d0=</AppID><ClientCRC>2CEDD161</ClientCRC><InnerID>00290000000190903013227358326090314912020000000000217381Y0E=</InnerID><IsRunningInEmulator>false</IsRunningInEmulator><ObtainChannel>CMB_ANDROID</ObtainChannel></PostData>"));
                addit("Algorithm", KeyManagerFactory.getDefaultAlgorithm());
                addit(" [+]", EncryptUtil.decryptcbc("eiUgfxeIByTNOMe+JoLO5A=="));
                addit(" [+]", EncryptUtil.decryptcbc("ZRFptM5TDbqdRZsSB9N2Kw=="));
                try {


                    KeyStore c = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                Log.d(TAG, "onClick: ");

        }
    }

    private void addit(String tag, String str) {
        Logd("addit:" + str);
        //创建TextView
        TextView textView = new TextView(MyActivity.this);
        textView.setTextColor(Color.RED);
        //设置显示内容
        textView.setText(tag + ":" + str);
        textView.setEnabled(true);

        //添加到LinearLayout中
        ll_content.addView(textView);


        scrollView.post(new Runnable() {
            @Override
            public void run() {
                //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView.pageScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }


    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            if (filename.contains("yeecall")) {
                try {

                    in = assetManager.open(filename);
                    File outFile = new File(getExternalFilesDir(null), filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename + e.getMessage());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private String ReadUserInfo() {
//        FileUtils.readFileFromSDCard("/sdcard/")
//        return "5866D577AC37B6B69033D9F915127CA15909CE8C449628484D87E00A57763824B0D86827AB6FAD64E154C50A4EF3F4A6400AFBE3594018FC492EC23FA822370BF8D48663913BA5408D89D101B06885E8F61A70A9806A8BB1514E49DACA20DF4687AE3BA5A0761646FBEB4E34F6D567F483309A90FC288DE3BC9F91206947187419BB7C4A13CFF802DDC2C2BFB6DFF40B3B18DD401D87F444D6B1212836C21709E2D1E9DAC79DD6BB0A351DD0B51CFE20075CF9603972722E3674C11C42E15926D477D4864F54F6E2A2AE24CF0CFB441FF367B343E675C53DACC4105A33E3C8C34DC7DCE5A6A060810727C00625A6D26F15262921FBA4A6AC9BBFF8944BE955E8324E03D20709E1D869A1B9D416F498EB4C2F2E8BD77BAAD42F21887FD73D8F5591440BF8355C07C0F0BFE74D4042D904";
//        return "D68DBFF8A8E300A855F10E79F79777B8BA5F46DC38646BD2FE0762B9AC53007889249D691E7011503BD1688DB6D0D38FB74F25F1579E0A6584E1E878E8A0CFEF99F0C6BF55B528EAEA49A99C4D5A46DBFF483988CFBDA0D3592E238D0B84CEBAF0B83A05F07127048338E7220ECF96C6ADC7E5F468FEBB9A2C53F573F507DF1FD42C3E902718F2E3FC1208159DD75BE2DA94A2BBCBBE9C1FA603B35FF774E6A79B4C8A4DDCA54020EC65A1AAB99144D51F842BDDBF772FE0673C704BDE5710E35D41541A1CD132DB4D742DDB40F5516DACFCBF978C00F3BC39CAF3A4353DA2C1DBE7CB43DA4E0000B9277E428D40C692A5CB7739DCBED473858556146FFF92C7";
        return "D68DBFF8A8E300A855F10E79F79777B8BA5F46DC38646BD2FE0762B9AC530078B74EF0E2AE183832D4B05D15455CE3AEE61ED0204E4EC4DF613910C69ED0F1701DCD095DD14A4210D4440A390D1EB8CFBCE779D3F6EB1EF0C0056030C9DF0C6D587E67ADD847C1E42A91AADAE8B02D81DC0C4A3AB789C97ACCA2EBA0F5923EB38987DFC6A4984AB8499CCF1BC550FB9B88962B7E97A8FAC391DA79578B024F0FD0D3C2FCDFF71CF3BC91E2509153F20843E19F72D63AE0A2193ED3CE7E2414B4FDB382B9CE41D1781755F422B9CB7020ED2737992F30F4A5D480F2EA5121FFFAC075AC01A42352A117FF62973140A446";
    }

    private String jiemi(String str) {
        try {
            return aesUnEnc("@w#a$q&ejuak", unencry(str));

        } catch (Throwable tb) {
            tb.printStackTrace();
        }
        return "";
    }

    public final byte[] unencry(java.lang.String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = java.lang.Integer.valueOf(str.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return bArr;
    }

    private static byte[] initkey(java.lang.String str) throws java.lang.Exception {

        return new javax.crypto.spec.SecretKeySpec(javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new javax.crypto.spec.PBEKeySpec(str.toCharArray(), "!q@w#e$r%t^y#n@v".getBytes(), 10, 128)).getEncoded(), "AES").getEncoded();
    }

    static java.lang.String aesUnEnc(String str, byte[] bArr) throws java.lang.Throwable {
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(initkey(str), "AES");
        javax.crypto.Cipher instance = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(DECRYPT_MODE, secretKeySpec, new javax.crypto.spec.IvParameterSpec("1653678145712191".getBytes()));
        return new java.lang.String(instance.doFinal(bArr));
    }


}
