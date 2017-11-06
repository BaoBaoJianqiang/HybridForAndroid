package com.example.jianqiang.hybridapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.jianqiang.hybridapp.tools.DirTraversal;
import com.example.jianqiang.hybridapp.tools.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipException;

public class MainActivity extends AppCompatActivity {

    private static final String pathString = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final Map<String[], Integer> PERMISSION_MAP = new HashMap<String[], Integer>() {
        {
            put(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //进入到WebView页面
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrRequestPermission(PERMISSIONS_STORAGE)) {
                    unzip2Sd();
                }
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    unzip2Sd();
                } else {
                    // User disagree the permission
                    Toast.makeText(this, "您拒绝授予本APP SD卡使用权限", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private boolean checkOrRequestPermission(String[] permissions) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            // Do not have the permission, request it.
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_MAP.get(permissions));
            return false;
        } else {
            // Have gotten the permission
            return true;
        }
    }

    private void unzip2Sd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZipUtils.unApkFile(getApplication(), "hybrid.zip", pathString+ File.separator + "hybrid", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
