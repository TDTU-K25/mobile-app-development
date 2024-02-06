package com.example.ex1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnToast;
    Button btnCount;
    TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToast = findViewById(R.id.btn_toast);
        btnCount = findViewById(R.id.btn_count);
        tvCount = findViewById(R.id.tv_count);

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvCount.getText().toString();
                int currentCount = Integer.parseInt(data);
                int nextCount = currentCount + 1;
                tvCount.setText(String.valueOf(nextCount));
            }
        });

        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), tvCount.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }
}