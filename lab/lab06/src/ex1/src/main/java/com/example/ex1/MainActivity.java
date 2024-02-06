package com.example.ex1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    LinearLayout llBackground;
    TextView tvCounter;
    EditText etTextColor;
    EditText etBackgroundColor;
    Button btnSave;

    int counter;
    String textColor;
    String backgroundColor;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llBackground = findViewById(R.id.ll_background);
        tvCounter = findViewById(R.id.tv_counter);
        etTextColor = findViewById(R.id.et_textColor);
        etBackgroundColor = findViewById(R.id.et_backgroundColor);
        btnSave = findViewById(R.id.btn_save);

        this.sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();

        getDataFromSharedPreferencesAndAssignToWidgets();

        increaseCounter();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToSharedPreferences();
                Toast.makeText(MainActivity.this, "Save successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDataFromSharedPreferencesAndAssignToWidgets() {
        counter = sharedPref.getInt(getString(R.string.saved_counter_key), 1);
        textColor = sharedPref.getString(getString(R.string.saved_text_color_key), "#FFFFFF");
        backgroundColor = sharedPref.getString(getString(R.string.saved_background_color_key), "#2222FF");

        etBackgroundColor.setText(backgroundColor);
        etTextColor.setText(textColor);
        llBackground.setBackgroundColor(Color.parseColor(backgroundColor));
        tvCounter.setText(String.valueOf(counter));
        tvCounter.setTextColor(Color.parseColor(textColor));
    }

    private void saveToSharedPreferences() {
        this.editor.putString(getString(R.string.saved_text_color_key), etTextColor.getText().toString());
        this.editor.putString(getString(R.string.saved_background_color_key), etBackgroundColor.getText().toString());
        this.editor.apply();
    }

    private void increaseCounter() {
        this.editor.putInt(getString(R.string.saved_counter_key), counter + 1);
        this.editor.apply();
    }
}