package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logInfo("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logInfo("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logInfo("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logInfo("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logInfo("onDestroy");
    }

    private void logInfo(String msg) {
        Log.i("MainActivityLifecycle", msg);
    }
}