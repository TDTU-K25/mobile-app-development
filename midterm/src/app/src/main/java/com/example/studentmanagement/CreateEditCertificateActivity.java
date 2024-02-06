package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.model.Certificate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CreateEditCertificateActivity extends AppCompatActivity {
    final static String STUDENTS = "Students";
    final static String CERTIFICATES = "Certificates";
    TextView tvTitle;
    EditText etSubject;
    EditText etCompletionDate;
    EditText etOrganization;
    Button btnSave;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_certificate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tv_title);
        etSubject = findViewById(R.id.et_subject);
        etOrganization = findViewById(R.id.et_organization);
        etCompletionDate = findViewById(R.id.et_completion_date);
        btnSave = findViewById(R.id.btn_save);
        etCompletionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateDialog();
            }
        });

        Intent intent = getIntent();
        // if action is edit => get data from firestore and fill in all fields
        if (intent != null && intent.getStringExtra("action").equals("edit")) {
            String studentUID = intent.getStringExtra("studentUID");
            String certificateUID = intent.getStringExtra("certificateUID");
            firebaseFirestore.collection(STUDENTS).document(studentUID).collection(CERTIFICATES).document(certificateUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    tvTitle.setText("Edit Certificate");
                    btnSave.setText("Save");
                    etSubject.setText((String) documentSnapshot.get("subject"));
                    etOrganization.setText((String) documentSnapshot.get("organization"));
                    etCompletionDate.setText(String.valueOf(documentSnapshot.getTimestamp("completionDate").toDate()));
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
                String subject = etSubject.getText().toString();
                String organization = etOrganization.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date completionDate = null;
                try {
                    completionDate = dateFormat.parse(etCompletionDate.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (intent != null && intent.getStringExtra("action").equals("edit")) {
                    String studentUID = intent.getStringExtra("studentUID");
                    String certificateUID = intent.getStringExtra("certificateUID");
                    Certificate certificate = new Certificate(certificateUID, subject, completionDate, organization);
                    firebaseFirestore.collection(STUDENTS).document(studentUID).collection(CERTIFICATES).document(certificateUID).set(certificate);
                    finish();
                } else {
                    String studentUID = intent.getStringExtra("studentUID");
                    Certificate certificate = new Certificate(subject, completionDate, organization);
                    firebaseFirestore.collection(STUDENTS).document(studentUID).collection(CERTIFICATES).add(certificate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            certificate.setId(documentReference.getId());
                            documentReference.set(certificate);
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

    private void selectDateDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etCompletionDate.setText(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.of(year, month + 1, dayOfMonth)));
            }
        };

        DatePickerDialog dialog;
        dialog = new DatePickerDialog(this, onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }
}