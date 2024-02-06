package com.example.ex1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                boolean isValidPassword = checkPassword(password);
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập username hoặc password", Toast.LENGTH_LONG).show();
                } else if (username.equals("admin") && password.equals("admin1234")) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6 || !isValidPassword) {
                    Toast.makeText(MainActivity.this, "Mật khẩu không đúng yêu cầu", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập username", Toast.LENGTH_LONG).show();
                } else if (username.equals("admin")) {
                    Toast.makeText(MainActivity.this, "Reset mật khẩu thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}