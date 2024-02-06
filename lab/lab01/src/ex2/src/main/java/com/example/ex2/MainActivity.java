package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etContent;
    Button btnSubmit;

    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = findViewById(R.id.et_content);
        btnSubmit = findViewById(R.id.btn_submit);
        tvContent = findViewById(R.id.tv_content);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etContent.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "vui lòng nhập thông tin", Toast.LENGTH_LONG).show();
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(etContent.getText());
                }
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etContent.getText().toString().toLowerCase(Locale.ROOT).equals("off")) {
                    btnSubmit.setEnabled(false);
                } else if (etContent.getText().toString().toLowerCase(Locale.ROOT).equals("on")) {
                    btnSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}