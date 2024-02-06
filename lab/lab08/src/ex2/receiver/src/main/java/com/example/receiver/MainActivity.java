package com.example.receiver;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // need to run the app once so have the app installed on the device
        // then it should work

        IntentFilter itf = new IntentFilter();
        itf.addAction("com.example.ex2.action_send_msg");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, itf);
    }
}