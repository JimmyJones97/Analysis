package com.example.pwd61.analysis;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pwd61.analysis.Utils.Utils;
import com.example.pwd61.analysis.app.Yeecall;
import com.example.pwd61.analysis.app.yeecall.CipherProtocol;
import com.example.pwd61.analysis.app.yeecall.HashUtils;
import com.example.pwd61.analysis.app.yeecall.HexUtils;
import com.example.pwd61.analysis.app.yeecall.PreferencesImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import static com.example.pwd61.analysis.Utils.Utils.Logd;


public class MyActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "HACK";
    Button insert;
    Button query;
    Button update;
    Button delete;
    Button querys;
    Uri uri = Uri.parse("content://hb.android.contentProvider/teacher");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button jiemi = findViewById(R.id.decrypt);
        Button tst1 = findViewById(R.id.Test1);
        Button tst2 = findViewById(R.id.Test2);
        jiemi.setOnClickListener(this);
        tst2.setOnClickListener(this);
        tst1.setOnClickListener(this);
        insert = (Button) findViewById(R.id.insert);
        query = (Button) findViewById(R.id.query);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        querys = (Button) findViewById(R.id.querys);
        insert.setOnClickListener(new InsertListener());
        query.setOnClickListener(new QueryListener());
        // 方法二
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ContentResolver cr = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put("name", "huangbiao");
                cv.put("date_added", (new Date()).toString());
                int uri2 = cr.update(uri, cv, "_ID=?", new String[]{"3"});
                System.out.println("updated" + ":" + uri2);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ContentResolver cr = getContentResolver();
                cr.delete(uri, "_ID=?", new String[]{"2"});
            }
        });

        querys.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                ContentResolver cr = getContentResolver();
                // 查找id为1的数据
                Cursor c = cr.query(uri, null, null, null, null);
                System.out.println(c.getCount());
                c.close();
            }
        });
        copyAssets();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.decrypt:
                int ss = HashUtils.SHA1("a834d1d61d7b2c9d7072adb704dab86010712b5b9a731ec5d2ed7ea2bd61832c4f085c51b6fcd948adde96f0067fc06d672acedc3d72f2db155455bd4cdef73e").toCharArray().length;
                Log.d(TAG, "onClick: 解密->" + ss);
                Log.d(TAG, "解密-->" + HashUtils.SHA1("50f4bb718e0772932cbc7342c6295b4826c353f67181e560c3375a5ccae62e9bzayhu.main.settings"));
                Log.d(TAG, "TEST:" + CipherProtocol.a("abc"));

                String dbName = "yeecall.sp";
                String STR = dbName + "tcfb3352c2df335696c6bc631932c6a61a4cdf318";
                String dbNameEnc = CipherProtocol.a(STR);
                Logd("数据库名字：" + dbName + ",enc: " + dbNameEnc);

                String stringBuilder2 = dbNameEnc + "t72f283666ae9a3482660515b0f9acebeaff91e04";
                String columnsID = CipherProtocol.a(stringBuilder2);
                Logd("加密列：" + columnsID);
                Logd("test ："+PreferencesImpl.b("1".toCharArray()));
                Logd("test1 :"+ PreferencesImpl.b("1234".toCharArray()));
                Yeecall.getKey(getApplicationContext());
                break;
            case R.id.Test1:
                Log.d(TAG, "测试1");
                break;
            case R.id.Test2:
                Log.d(TAG, "测试2");
                break;
            default:
                Log.d(TAG, "onClick: ");

        }
    }


    class InsertListener implements View.OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            ContentResolver cr = getContentResolver();

            ContentValues cv = new ContentValues();
            cv.put("title", "jiaoshou");
            cv.put("name", "jiaoshi");
            cv.put("sex", true);
            Uri uri2 = cr.insert(uri, cv);
            System.out.println(uri2.toString());
        }

    }

    class QueryListener implements View.OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            ContentResolver cr = getContentResolver();
            // 查找id为1的数据
            Cursor c = cr.query(uri, null, "_ID=?", new String[]{"1"}, null);
            //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
            if (c.moveToFirst() == false) {
                // 为空的Cursor
                return;
            }
            int name = c.getColumnIndex("name");
            System.out.println(c.getString(name));
            c.close();
        }
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
            if(filename.contains("yeecall")) {
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
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
