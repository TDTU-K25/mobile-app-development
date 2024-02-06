package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateEditUserActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etEmail;
    EditText etName;
    EditText etAge;
    EditText etPhoneNumber;
    RadioGroup rgRole;
    RadioButton rbEmployee;
    RadioButton rbManager;
    Button btnSave;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tv_title);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etPhoneNumber = findViewById(R.id.et_phoneNumber);
        rgRole = findViewById(R.id.rg_role);
        rbEmployee = findViewById(R.id.rb_employee);
        rbManager = findViewById(R.id.rb_manager);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        // if action is edit => get data from firestore and fill in all fields
        if (intent != null && intent.getStringExtra("action").equals("edit")) {
            etEmail.setVisibility(View.INVISIBLE);
            String uid = intent.getStringExtra("uid");
            firebaseFirestore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    tvTitle.setText("Edit User");
                    btnSave.setText("Save");
                    etName.setText((String) documentSnapshot.get("name"));
                    etAge.setText(String.valueOf((long) documentSnapshot.get("age")));
                    etPhoneNumber.setText((String) documentSnapshot.get("phoneNumber"));
                    int role = ((Long) documentSnapshot.get("role")).intValue();
                    if (role == 0) {
                        rbEmployee.setChecked(true);
                    } else if (role == 1) {
                        rbManager.setChecked(true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = email.split("@")[0];
                String name = etName.getText().toString();
                int age = Integer.valueOf(etAge.getText().toString());
                String phoneNumber = etPhoneNumber.getText().toString();

                RadioButton radioButtonSelected = findViewById(rgRole.getCheckedRadioButtonId());
                int role = (radioButtonSelected.getText().toString().equals("Employee") ? 0 : 1);

                if (intent != null && intent.getStringExtra("action").equals("edit")) {
                    String uid = intent.getStringExtra("uid");
                    User user = new User(uid, name, age, phoneNumber, role);
                    firebaseFirestore.collection("Users").document(uid).set(user);
                    finish();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String uid = authResult.getUser().getUid();
                            // use collectionReference will not have the same UID
                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
                            User user = new User(uid, name, age, phoneNumber, role);
                            documentReference.set(user);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}