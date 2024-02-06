package com.example.ex3;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class MyBrowserActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browser);

        wv = findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        wv.loadUrl(String.valueOf(intent.getData()));
    }
}