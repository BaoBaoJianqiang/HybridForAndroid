package com.example.jianqiang.hybridapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        parseUriParams();
    }


    private void parseUriParams() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            Toast.makeText(this, "from:" + uri.getQueryParameter("from"), Toast.LENGTH_SHORT).show();
        }
    }
}
