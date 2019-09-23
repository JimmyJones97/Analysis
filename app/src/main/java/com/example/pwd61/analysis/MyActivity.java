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

import cmb.pb.shield.whitebox.EncryptUtil;

import static com.example.pwd61.analysis.Utils.utils.Logd;


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
                String data="a834d1d61d7b2c9d7072adb704dab86010712b5b9a731ec5d2ed7ea2bd61832c4f085c51b6fcd948adde96f0067fc06d672acedc3d72f2db155455bd4cdef73e";
                String SS=HashUtils.SHA1(data);
                addit("yee",SS);
                addit("tyee",CipherProtocol.a("c_db_kvs_xxxxxslat"));

                IKeyValueStorage IKS=ZayhuPref.a(getApplicationContext(),"c_db_kvs_xxxxx",5);



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

                break;
            case R.id.Test1:
                Log.d(TAG, "测试1");
                break;
            case R.id.Test2:
                Log.d(TAG, "测试2");
                break;
            case R.id.ctf:
                Log.d(TAG, "ctf");
                addit("ctf",!Utils.cec("10864017","1234")?"succ":"failed!");
                addit("[+]" , EncryptUtil.decryptcbc("z/GU3NDcvTToe21svo0+KQ=="));
                break;
            case R.id.cmb:
                Log.d(TAG, "cmb");
                //utils.Logd(Utils.cec("10864017","1234")==false?"succ":"failed!");
                addit(" [+]" ,EncryptUtil.decryptcbc("z/GU3NDcvTToe21svo0+KQ=="));
                addit("shijiea ", PBRsa.a("WcXqq0aNxzVLVxKi"));
                addit("shijiea ", PBRsa.a("QhVi0qpMKXJ3P3M5"));
                addit("aes", AesUtils.encrypt("<PostData><DeviceType>E</DeviceType><Version>7.2.0</Version><SystemVersion>9</SystemVersion><ExtraFormat>PNG</ExtraFormat><AppID>0029000000020190903013227020000000000q5Tr3xiEc3AUByxNbqrru3t9d0=</AppID><ClientCRC>2CEDD161</ClientCRC><InnerID>00290000000190903013227358326090314912020000000000217381Y0E=</InnerID><IsRunningInEmulator>false</IsRunningInEmulator><ObtainChannel>CMB_ANDROID</ObtainChannel></PostData>"));
                break;
            default:
                Log.d(TAG, "onClick: ");

        }
    }

    private void addit(String tag,String str) {
        Logd("addit:"+str);
        //创建TextView
        TextView textView = new TextView(MyActivity.this);
        textView.setTextColor(Color.RED);
        //设置显示内容
        textView.setText(tag+":"+str);
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
}
