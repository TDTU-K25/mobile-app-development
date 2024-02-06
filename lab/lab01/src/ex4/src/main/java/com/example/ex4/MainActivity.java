package com.example.ex4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUSD;
    EditText etEuros;
    EditText etVND;
    Button btnClear;
    Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUSD = findViewById(R.id.et_USD);
        etEuros = findViewById(R.id.et_Euros);
        etVND = findViewById(R.id.et_VND);
        btnClear = findViewById(R.id.btn_clear);
        btnConvert = findViewById(R.id.btn_convert);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUSD.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "vui lòng nhập thông tin", Toast.LENGTH_LONG).show();
                    return;
                }
                Double usd = Double.parseDouble(etUSD.getText().toString());
                etEuros.setText(String.valueOf(Math.round(usd2Euros(usd) * 100.0) / 100.0));
                etVND.setText(String.valueOf(usd2VND(usd)));
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUSD.setText("");
                etEuros.setText("");
                etVND.setText("");
            }
        });
    }

    private Double usd2Euros(double usd) {
        return usd * 0.94;
    }

    private Double usd2VND(double usd) {
        return usd * 24265;
    }
}