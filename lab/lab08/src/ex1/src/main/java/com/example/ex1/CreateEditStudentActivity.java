package com.example.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class CreateEditStudentActivity extends AppCompatActivity {
    TextInputLayout tilId, tilName, tilEmail, tilPhone;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_student);

        tilId = findViewById(R.id.til_id);
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action.equals("add")) {
            tilId.setVisibility(View.GONE);
        } else if (action.equals("edit")) {
            tilId.setVisibility(View.VISIBLE);
            tilId.getEditText().setText(intent.getIntExtra("id", -1) + "");
            tilName.getEditText().setText(intent.getStringExtra("name"));
            tilEmail.getEditText().setText(intent.getStringExtra("email"));
            tilPhone.getEditText().setText(intent.getStringExtra("phone"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals("add")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", tilName.getEditText().getText().toString());
                    resultIntent.putExtra("email", tilEmail.getEditText().getText().toString());
                    resultIntent.putExtra("phone", tilPhone.getEditText().getText().toString());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (action.equals("edit")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("position", intent.getIntExtra("position", -1));
                    resultIntent.putExtra("name", tilName.getEditText().getText().toString());
                    resultIntent.putExtra("email", tilEmail.getEditText().getText().toString());
                    resultIntent.putExtra("phone", tilPhone.getEditText().getText().toString());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}