package com.example.metube;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditChannelActivity extends AppCompatActivity {
    private final int CHOOSE_IMAGE_REQUEST = 1;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    EditText etName;
    EditText etAge;
    EditText etPhoneNumber;
    EditText etEmail;
    Button btnEdit;
    ImageView ivCamera;
    CircleImageView civAvatarEdit;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_channel);

        ivCamera = findViewById(R.id.iv_camera);
        btnEdit = findViewById(R.id.btn_edit);
        civAvatarEdit = findViewById(R.id.civ_avatar_edit);
        etName = findViewById(R.id.et_name);
        etPhoneNumber = findViewById(R.id.et_phoneNumber);
        etEmail = findViewById(R.id.et_email_channel);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        firestore.collection(getString(R.string.users_collection)).document(userID).get().addOnSuccessListener(documentSnapshot -> {
            etName.setText(documentSnapshot.getString("name"));
            etPhoneNumber.setText(documentSnapshot.getString("phoneNumber"));
            etEmail.setText(documentSnapshot.getString("email"));
            Glide.with(this).load(documentSnapshot.getString("avatar")).into(civAvatarEdit);
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // implicit intent
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "images/avatars/" + System.currentTimeMillis() + ".png";
                StorageReference storageReference = firebaseStorage.getReference(location);
                storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // then save information to firestore
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ; // wait until uriTask is successful
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()) {
                            // save to firestore
                            String name = etName.getText().toString();
                            String phoneNumber = etPhoneNumber.getText().toString();
                            try {
                                firestore.collection(getString(R.string.users_collection)).document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        documentSnapshot.getReference().update("name", name);
                                        documentSnapshot.getReference().update("phoneNumber", phoneNumber);
                                        documentSnapshot.getReference().update("avatar", downloadUri.toString());
                                        Toast.makeText(EditChannelActivity.this, "Edit successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditChannelActivity.this, VideoActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditChannelActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditChannelActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData(); // return the path where avatar is stored
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                civAvatarEdit.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}