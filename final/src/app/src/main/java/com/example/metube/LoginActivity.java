package com.example.metube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextUserName;
    private TextInputEditText editTextPassword;
    private Button btnLogin;
    private TextView textViewToRegister;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadElement();

        textViewToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(userName, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                LoginActivity.this.sharedPref = getSharedPreferences(getString(R.string.preference_login_key), Context.MODE_PRIVATE);
                                LoginActivity.this.editor = sharedPref.edit();

                                LoginActivity.this.editor.putString(getString(R.string.saved_account_uid_key), authResult.getUser().getUid());
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, VideoActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void loadElement() {
        editTextPassword = findViewById(R.id.txt_password);
        editTextUserName = findViewById(R.id.txt_email);
        btnLogin = findViewById(R.id.btn_login);
        textViewToRegister = findViewById(R.id.txt_go_to_register);
    }
}