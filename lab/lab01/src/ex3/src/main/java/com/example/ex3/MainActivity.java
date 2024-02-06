package com.example.ex3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Double percentTip = 0.1;
    EditText etBillAmount;
    TextView tvPercent;
    Button btnDecrease;
    Button btnIncrease;
    TextView tvTip;
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBillAmount = findViewById(R.id.et_billAmount);
        tvPercent = findViewById(R.id.tv_percent);
        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);
        tvTip = findViewById(R.id.tv_tip);
        tvTotal = findViewById(R.id.tv_total);

        etBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateTipAndTotalUI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Math.round(percentTip * 100.0) / 100.0 == 0.05) {
                    btnDecrease.setEnabled(false);
                }
                percentTip -= 0.01;
                updateTipAndTotalUI();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDecrease.setEnabled(true);
                percentTip += 0.01;
                updateTipAndTotalUI();
            }
        });
    }

    private void updateTipAndTotalUI() {
        String data = etBillAmount.getText().toString();
        if (data.isEmpty()) {
            data = "0";
        }
        Double billAmount = Double.parseDouble(data);
        tvPercent.setText((int) Math.ceil(percentTip * 100) + "%");
        tvTip.setText("$" + Math.round(calculateTip(billAmount, percentTip) * 10.0) / 10.0);
        tvTotal.setText("$" + Math.round(calculateTotal(billAmount, percentTip) * 10.0) / 10.0);
    }

    private Double calculateTotal(Double billAmount, Double tipPercent) {
        return billAmount + calculateTip(billAmount, tipPercent);
    }

    private Double calculateTip(Double billAmount, Double tipPercent) {
        return billAmount * tipPercent;
    }
}