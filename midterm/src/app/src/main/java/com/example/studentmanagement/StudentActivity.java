package com.example.studentmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.adapter.StudentAdapter;
import com.example.studentmanagement.model.Student;
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
import java.util.List;

public class StudentActivity extends AppCompatActivity implements StudentAdapter.OnStudentClickListener {
    final static int CHOOSE_FILE_REQUEST_CODE = 1;
    final static String STUDENTS = "Students";
    CheckBox cbName;
    CheckBox cbAge;
    CheckBox cbGpa;
    EditText etName;
    EditText etAge;
    EditText etGpa;
    Button btnSort;
    Button btnSearch;
    Button btnReset;
    RecyclerView rv;
    Button btnImportStudents;
    Button btnExportStudents;
    StudentAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference studentsRef = firebaseFirestore.collection(STUDENTS);
    SharedPreferences sharedPref;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        rv = findViewById(R.id.rv_students);
        cbName = findViewById(R.id.cb_name);
        cbAge = findViewById(R.id.cb_age);
        cbGpa = findViewById(R.id.cb_gpa);
        btnSort = findViewById(R.id.btn_sort);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etGpa = findViewById(R.id.et_gpa);
        btnSearch = findViewById(R.id.btn_search);
        btnReset = findViewById(R.id.btn_reset);
        btnImportStudents = findViewById(R.id.btn_import_students);
        btnExportStudents = findViewById(R.id.btn_export_students);

        // Get role and update UI based on role
        this.sharedPref = getSharedPreferences(getString(R.string.preference_login_key), Context.MODE_PRIVATE);
        role = sharedPref.getInt(getString(R.string.saved_role_key), 0);
        if (role == 0) {
            btnImportStudents.setVisibility(View.INVISIBLE);
            btnExportStudents.setVisibility(View.INVISIBLE);
        }

        btnImportStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
            }
        });

        btnExportStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        exportStudentsToFileOnExternalStorage(queryDocumentSnapshots.getDocuments());
                        Toast.makeText(StudentActivity.this, "Export successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        // firestore has limit in search for multiple criteria
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = studentsRef;
                String name = etName.getText().toString();
                if (!name.isEmpty()) {
                    query = query.whereEqualTo("name", name);
                }
                if (!etAge.getText().toString().isEmpty()) {
                    int age = Integer.parseInt(etAge.getText().toString());
                    query = query.whereEqualTo("age", age);
                } else if (!etGpa.getText().toString().isEmpty()) {
                    double gpa = Double.parseDouble(etGpa.getText().toString());
                    query = query.whereEqualTo("gpa", gpa);
                }

                FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>().setQuery(query, Student.class).build();
                adapter.updateOptions(options);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = studentsRef;
                if (cbName.isChecked()) {
                    query = query.orderBy("name", Query.Direction.ASCENDING);
                }
                if (cbAge.isChecked()) {
                    query = query.orderBy("age", Query.Direction.ASCENDING);
                }
                if (cbGpa.isChecked()) {
                    query = query.orderBy("gpa", Query.Direction.ASCENDING);
                }
                FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>().setQuery(query, Student.class).build();
                adapter.updateOptions(options);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = studentsRef;
                FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>().setQuery(query, Student.class).build();
                adapter.updateOptions(options);
            }
        });

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        // set adapter
        Query query = studentsRef;
        // simply get the data from query to the adapter
        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>().setQuery(query, Student.class).build();
        adapter = new StudentAdapter(options, this);
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
                    List<Student> students = new ArrayList<>();
                    reader.readLine(); // remove the row headers
                    while ((row = reader.readLine()) != null) {
                        String[] cols = row.split(",");
                        Student student = new Student(cols[0], Integer.parseInt(cols[1]), Boolean.valueOf(cols[2]), cols[3], cols[4], Double.valueOf(cols[5]));
                        students.add(student);
                    }
                    reader.close();
                    for (Student student : students) {
                        studentsRef.add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                student.setId(documentReference.getId());
                                documentReference.set(student);
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
            Intent intent = new Intent(StudentActivity.this, CreateEditStudentActivity.class);
            intent.putExtra("action", "add");
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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onEditStudentClick(String uid) {
        if (role == 0) {
            Toast.makeText(this, "You do not have permission", Toast.LENGTH_LONG).show();
        } else if (role == 1 || role == 2) {
            Intent intent = new Intent(this, CreateEditStudentActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("action", "edit");
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteStudentClick(String uid) {
        if (role == 0) {
            Toast.makeText(this, "You do not have permission", Toast.LENGTH_LONG).show();
        } else if (role == 1 || role == 2) {
            studentsRef.document(uid).delete();
        }
    }

    @Override
    public void onSeeStudentDetailedInfoClick(String uid) {
        Intent intent = new Intent(this, StudentDetailActivity.class);
        intent.putExtra("studentUID", uid);
        startActivity(intent);
    }

    private void exportStudentsToFileOnExternalStorage(List<DocumentSnapshot> documentSnapshots) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        try {
            FileWriter writer = new FileWriter(new File(path, "students.csv"));
            List<String> rowHeaders = new ArrayList<>();
            rowHeaders.add("Name");
            rowHeaders.add("Age");
            rowHeaders.add("IsMale");
            rowHeaders.add("PhoneNumber");
            rowHeaders.add("Email");
            rowHeaders.add("GPA");
            writer.append(String.join(",", rowHeaders));
            writer.append("\n");
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                String name = (String) documentSnapshot.get("name");
                String age = (String.valueOf((long) documentSnapshot.get("age")));
                String isMale = String.valueOf(documentSnapshot.get("male"));
                String phoneNumber = (String) documentSnapshot.get("phoneNumber");
                String email = (String) (documentSnapshot.get("email"));
                String gpa = (String.valueOf(documentSnapshot.get("gpa")));

                List<String> row = new ArrayList<>();
                row.add(name);
                row.add(age);
                row.add(isMale);
                row.add(phoneNumber);
                row.add(email);
                row.add(gpa);
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