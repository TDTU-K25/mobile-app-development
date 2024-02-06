package com.example.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    TextView tvMsg;
    EditText etName;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvMsg = findViewById(R.id.tv_msg);
        etName = findViewById(R.id.et_name);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        tvMsg.setText(String.format("Xin chào, %s. Vui lòng nhập tên", email));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra("name", etName.getText().toString());
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}