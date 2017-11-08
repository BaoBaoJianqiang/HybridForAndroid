package com.example.jianqiang.hybridapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jianqiang.hybridapp.tools.Utils;
import com.example.jianqiang.hybridapp.tools.ZipUtils;
import com.cundong.utils.PatchUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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



    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private static final int FLAG_SUCCESS = 0X00;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            File newFile = new File(mNewFilePath);
            Utils.installApk(MainActivity.this,newFile);
        }
    };


    static {
        System.loadLibrary("ApkPatchLibrary");
    }

    private String mNewFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions();
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (checkOrRequestPermission(PERMISSIONS_STORAGE)) {
                            patchApk();
                        }
                    }
                }).start();

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

    private void patchApk() {
        File oldApk = this.getFileStreamPath("plug_old.apk");
        String oldApkPath = oldApk.getPath();
        File patchFile = this.getFileStreamPath("diff.patch");
        String patchFilePath = patchFile.getPath();

        mNewFilePath = Environment.getExternalStorageDirectory() + File.separator +"hybrid"+File.separator+ "new3.apk";

        try {
            int patchResult = PatchUtils.patch(oldApkPath, mNewFilePath, patchFilePath);
            if(patchResult == 0) {
                Log.e("bao", patchResult + "");
                handler.sendEmptyMessage(FLAG_SUCCESS);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Utils.extractAssets(newBase, "plug_old.apk");
        Utils.extractAssets(newBase, "diff.patch");
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
