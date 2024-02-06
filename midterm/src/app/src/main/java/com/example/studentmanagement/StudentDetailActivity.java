package com.example.studentmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.adapter.CertificateAdapter;
import com.example.studentmanagement.model.Certificate;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDetailActivity extends AppCompatActivity implements CertificateAdapter.OnCertificateClickListener {
    final static Integer CHOOSE_FILE_REQUEST_CODE = 1;
    final static String STUDENTS = "Students";
    final static String CERTIFICATES = "Certificates"; // sub collection
    RecyclerView rv;
    TextView tvName;
    EditText etAge;
    EditText etPhoneNumber;
    EditText etGender;
    EditText etEmail;
    EditText etGpa;
    Button btnImportCertificates;
    Button btnExportCertificates;
    CertificateAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference studentsRef = firebaseFirestore.collection(STUDENTS);
    String studentUID;
    SharedPreferences sharedPref;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        rv = findViewById(R.id.rv_certificate);
        tvName = findViewById(R.id.tv_name);
        etAge = findViewById(R.id.et_age);
        etPhoneNumber = findViewById(R.id.et_phoneNumber);
        etGender = findViewById(R.id.et_gender);
        etEmail = findViewById(R.id.et_email);
        etGpa = findViewById(R.id.et_gpa);
        btnImportCertificates = findViewById(R.id.btn_import_certificates);
        btnExportCertificates = findViewById(R.id.btn_export_certificates);

        // Get role and update UI based on role
        this.sharedPref = getSharedPreferences(getString(R.string.preference_login_key), Context.MODE_PRIVATE);
        role = sharedPref.getInt(getString(R.string.saved_role_key), 0);
        if (role == 0) {
            btnImportCertificates.setVisibility(View.INVISIBLE);
            btnExportCertificates.setVisibility(View.INVISIBLE);
        }

        btnImportCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
            }
        });

        btnExportCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentsRef.document(studentUID).collection(CERTIFICATES).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        exportCertificatesToFileOnExternalStorage(queryDocumentSnapshots.getDocuments());
                        Toast.makeText(StudentDetailActivity.this, "Export successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        studentUID = intent.getStringExtra("studentUID");
        firebaseFirestore.collection(STUDENTS).document(studentUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                etEmail.setText((String) documentSnapshot.get("email"));
                tvName.setText((String) documentSnapshot.get("name"));
                etAge.setText(String.valueOf((long) documentSnapshot.get("age")));
                etPhoneNumber.setText((String) documentSnapshot.get("phoneNumber"));
                etGpa.setText(String.valueOf(documentSnapshot.get("gpa")));
                boolean isMale = (Boolean) documentSnapshot.get("male");
                if (isMale) {
                    etGender.setText("Male");
                } else {
                    etGender.setText("Female");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        // set adapter
        Query query = studentsRef.document(studentUID).collection(CERTIFICATES);

        // simply get the data from query to the adapter
        FirestoreRecyclerOptions<Certificate> options = new FirestoreRecyclerOptions.Builder<Certificate>().setQuery(query, Certificate.class).build();
        adapter = new CertificateAdapter(options, this);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String row = "";
                    List<Certificate> certificates = new ArrayList<>();
                    reader.readLine(); // remove the row headers
                    while ((row = reader.readLine()) != null) {
                        String[] cols = row.split(",");
                        Certificate certificate = new Certificate(cols[0], new Date(cols[2]), cols[1]);
                        certificates.add(certificate);
                    }
                    reader.close();
                    for (Certificate certificate : certificates) {
                        firebaseFirestore.collection(STUDENTS).document(studentUID).collection(CERTIFICATES).add(certificate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                certificate.setId(documentReference.getId());
                                documentReference.set(certificate);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_management, menu);
        // update UI based on role
        if (role == 0 || role == 1) {
            MenuItem itemAccountManagement = menu.findItem(R.id.account_management);
            itemAccountManagement.setVisible(false);
        }
        if (role == 0) {
            MenuItem itemAdd = menu.findItem(R.id.create);
            itemAdd.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String accountUID = sharedPref.getString(getString(R.string.saved_account_uid_key), "");
        if (item.getItemId() == R.id.create) {
            Intent intent = new Intent(StudentDetailActivity.this, CreateEditCertificateActivity.class);
            intent.putExtra("action", "add");
            intent.putExtra("studentUID", studentUID);
            startActivity(intent);
        } else if (item.getItemId() == R.id.account_management) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.student_management) {
            Intent intent = new Intent(this, StudentActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.info) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("accountUID", accountUID);
            startActivity(intent);
        } else if (item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditCertificateClick(String uid) {
        Intent intent = new Intent(this, CreateEditCertificateActivity.class);
        intent.putExtra("studentUID", studentUID);
        intent.putExtra("certificateUID", uid);
        intent.putExtra("action", "edit");
        startActivity(intent);
    }

    @Override
    public void onDeleteCertificateClick(String uid) {
        studentsRef.document(studentUID).collection(CERTIFICATES).document(uid).delete();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    private void exportCertificatesToFileOnExternalStorage(List<DocumentSnapshot> documentSnapshots) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        try {
            FileWriter writer = new FileWriter(new File(path, studentUID + "_certificates.csv"));
            List<String> rowHeaders = new ArrayList<>();
            rowHeaders.add("Subject");
            rowHeaders.add("Organization");
            rowHeaders.add("Completion date");
            writer.append(String.join(",", rowHeaders));
            writer.append("\n");
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                String subject = (String) documentSnapshot.get("subject");
                String organization = (String) documentSnapshot.get("organization");
                String completionDate = String.valueOf(documentSnapshot.getTimestamp("completionDate").toDate());
                List<String> row = new ArrayList<>();
                row.add(subject);
                row.add(organization);
                row.add(completionDate);
                writer.append(String.join(",", row));
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}