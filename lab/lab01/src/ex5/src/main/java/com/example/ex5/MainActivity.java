package com.example.ex5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    CheckBox checkBoxAndroid;
    CheckBox checkBoxIOS;
    CheckBox checkBoxWindows;
    CheckBox checkBoxRIM;
    Button btnSeeResultsCheckboxes;
    TextView tvResultCheckboxes;

    RadioButton radioButtonAndroid;
    RadioButton radioButtonIOS;
    RadioButton radioButtonWindows;
    RadioButton radioButtonRIM;
    Button btnSeeResultsRadioButtons;
    TextView tvResultRadioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBoxAndroid = findViewById(R.id.checkbox_android);
        checkBoxIOS = findViewById(R.id.checkbox_ios);
        checkBoxWindows = findViewById(R.id.checkbox_windows);
        checkBoxRIM = findViewById(R.id.checkbox_rim);
        btnSeeResultsCheckboxes = findViewById(R.id.btn_seeResultsCheckboxes);
        tvResultCheckboxes = findViewById(R.id.tv_resultsCheckboxes);

        btnSeeResultsCheckboxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String results = "The following were selected...\n" + "Android: " + checkBoxAndroid.isChecked() + "\n" + "iOS: " + checkBoxIOS.isChecked() + "\n" + "Windows: " + checkBoxWindows.isChecked() + "\n" + "RIM: " + checkBoxRIM.isChecked() + "\n";
                tvResultCheckboxes.setText(results);
            }
        });

        radioButtonAndroid = findViewById(R.id.radio_android);
        radioButtonIOS = findViewById(R.id.radio_ios);
        radioButtonWindows = findViewById(R.id.radio_windows);
        radioButtonRIM = findViewById(R.id.radio_rim);
        btnSeeResultsRadioButtons = findViewById(R.id.btn_seeResultsRadioButtons);
        tvResultRadioButtons = findViewById(R.id.tv_resultsRadioButtons);

        btnSeeResultsRadioButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String results = "The following were selected...\n" + "Android: " + radioButtonAndroid.isChecked() + "\n" + "iOS: " + radioButtonIOS.isChecked() + "\n" + "Windows: " + radioButtonWindows.isChecked() + "\n" + "RIM: " + radioButtonRIM.isChecked() + "\n";
                tvResultRadioButtons.setText(results);
            }
        });
    }
}