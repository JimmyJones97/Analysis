package com.example.pwd61.analysis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pwd61.analysis.Utils.HashUtils;


public class MyActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG="HACK";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button jiemi=findViewById(R.id.decrypt);
        jiemi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.decrypt:
                Log.d(TAG, "onClick: 解密->"+HashUtils.SHA1("a834d1d61d7b2c9d7072adb704dab86010712b5b9a731ec5d2ed7ea2bd61832c4f085c51b6fcd948adde96f0067fc06d672acedc3d72f2db155455bd4cdef73e"));
                break;
            default:
                Log.d(TAG, "onClick: ");

        }
    }
}
