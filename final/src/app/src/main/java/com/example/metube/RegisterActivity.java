package com.example.metube;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.metube.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView toLogin;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private TextInputEditText editTextFullname;
    private TextInputEditText editTextEmail;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadElement();
    }

    private void loadElement() {
        editTextEmail = findViewById(R.id.txt_email);
        editTextFullname = findViewById(R.id.txt_fullname);
        editTextConfirmPassword = findViewById(R.id.txt_confirm_password);
        editTextPassword = findViewById(R.id.txt_password);
        toLogin = findViewById(R.id.txt_go_to_login);
        btnRegister = findViewById(R.id.btn_register);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String fullname = editTextFullname.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải trên 6 ký tự", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password, fullname);
                }
            }
        });
    }

    private void registerUser(String email, String password, String fullname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công.", Toast.LENGTH_SHORT).show();

                String uid = authResult.getUser().getUid();
                // use collectionReference will not have the same UID
                DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
                User user = new User(uid, fullname, email, password);
                documentReference.set(user);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

}