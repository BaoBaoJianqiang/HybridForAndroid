package com.example.jianqiang.hybridapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webview = (WebView) findViewById(R.id.webview);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new MyWebChromeClient());

        webview.loadUrl("file:///android_asset/abc.html");
    }

    public class MyWebChromeClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();
            if (url != null && url.toString().startsWith("htyridapp://")) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(url.toString()));
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
