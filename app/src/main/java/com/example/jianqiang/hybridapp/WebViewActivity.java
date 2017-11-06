package com.example.jianqiang.hybridapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webview = (WebView)findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/abc.html");
    }
}
