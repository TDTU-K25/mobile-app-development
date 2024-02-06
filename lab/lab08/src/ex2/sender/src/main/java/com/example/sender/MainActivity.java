package com.example.sender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText etMsg;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMsg = findViewById(R.id.et_msg);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMsg.getText().toString();
                Intent intent = new Intent();
                intent.setAction("com.example.ex2.action_send_msg");
                intent.putExtra("msg", msg);
                intent.setComponent(new ComponentName("com.example.receiver", "com.example.receiver.MyReceiver"));
                sendBroadcast(intent);
            }
        });
    }
}