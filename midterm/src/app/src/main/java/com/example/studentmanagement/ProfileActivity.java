package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private final int CHOOSE_IMAGE_REQUEST = 1;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextView tvName;
    EditText etAge;
    EditText etPhoneNumber;
    EditText etRole;
    Button btnUpload;
    ImageView ivCamera;
    CircleImageView civAvatarEdit;
    String accountUID;
    private Uri imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivCamera = findViewById(R.id.iv_camera);
        btnUpload = findViewById(R.id.btn_upload);
        civAvatarEdit = findViewById(R.id.civ_avatar_edit);
        tvName = findViewById(R.id.tv_name);
        etAge = findViewById(R.id.et_age);
        etPhoneNumber = findViewById(R.id.et_phoneNumber);
        etRole = findViewById(R.id.et_role);

        Intent intent = getIntent();
        accountUID = intent.getStringExtra("accountUID");

        firestore.collection("Users").document(accountUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvName.setText((String) documentSnapshot.get("name"));
                etAge.setText(String.valueOf(((Long) documentSnapshot.get("age")).intValue()));
                etPhoneNumber.setText((String) documentSnapshot.get("phoneNumber"));
                int role = ((Long) documentSnapshot.get("role")).intValue();
                if (role == 0) {
                    etRole.setText("Employee");
                } else if (role == 1) {
                    etRole.setText("Manager");
                } else if (role == 2) {
                    etRole.setText("Admin");
                }

                String base64Avatar = (String) documentSnapshot.get("avatar");
                if (base64Avatar != null) {
                    byte[] binaryData = Base64.getDecoder().decode(base64Avatar);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                    civAvatarEdit.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateAvatarOfUser();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgPath = data.getData(); // return the path where avatar is stored
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgPath); // research this
                civAvatarEdit.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // implicit intent
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
    }

    private void updateAvatarOfUser() throws IOException {
        if (imgPath != null) {
            InputStream inputStream = getContentResolver().openInputStream(imgPath);
            byte[] avatar = getBytes(inputStream);

            // convert byte array to base64-encoded string
            String base64Avatar = Base64.getEncoder().encodeToString(avatar);

            Map<String, Object> data = new HashMap<>();
            data.put("avatar", base64Avatar);
            firestore.collection("Users").document(accountUID).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ProfileActivity.this, "Update avatar success", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}