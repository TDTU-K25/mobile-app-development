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

import com.example.studentmanagement.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateEditStudentActivity extends AppCompatActivity {
    final static String STUDENTS = "Students";
    TextView tvTitle;
    EditText etEmail;
    EditText etName;
    EditText etAge;
    EditText etPhoneNumber;
    EditText etGpa;
    RadioGroup rgGender;
    RadioButton rbMale;
    RadioButton rbFemale;
    Button btnSave;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_student);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tv_title);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etPhoneNumber = findViewById(R.id.et_phoneNumber);
        etGpa = findViewById(R.id.et_gpa);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        // if action is edit => get data from firestore and fill in all fields
        if (intent != null && intent.getStringExtra("action").equals("edit")) {
            String uid = intent.getStringExtra("uid");
            firebaseFirestore.collection(STUDENTS).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    tvTitle.setText("Edit Student");
                    btnSave.setText("Save");
                    etEmail.setText((String) documentSnapshot.get("email"));
                    etName.setText((String) documentSnapshot.get("name"));
                    etAge.setText(String.valueOf((long) documentSnapshot.get("age")));
                    etPhoneNumber.setText((String) documentSnapshot.get("phoneNumber"));
                    etGpa.setText(String.valueOf(documentSnapshot.get("gpa")));
                    boolean isMale = (Boolean) documentSnapshot.get("male");
                    if (isMale) {
                        rbMale.setChecked(true);
                    } else {
                        rbFemale.setChecked(true);
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
                String name = etName.getText().toString();
                int age = Integer.valueOf(etAge.getText().toString());
                String phoneNumber = etPhoneNumber.getText().toString();
                double gpa = Double.valueOf(etGpa.getText().toString());

                RadioButton radioButtonSelected = findViewById(rgGender.getCheckedRadioButtonId());
                boolean isMale = radioButtonSelected.getText().toString().equals("Male");

                if (intent != null && intent.getStringExtra("action").equals("edit")) {
                    String uid = intent.getStringExtra("uid");
                    Student student = new Student(uid, name, age, isMale, phoneNumber, email, gpa);
                    firebaseFirestore.collection(STUDENTS).document(uid).set(student);
                    finish();
                } else {
                    Student student = new Student(name, age, isMale, phoneNumber, email, gpa);
                    firebaseFirestore.collection(STUDENTS).add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            student.setId(documentReference.getId());
                            documentReference.set(student);
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