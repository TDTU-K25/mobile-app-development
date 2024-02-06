package com.example.ex4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    ImageView ivAvatar;
    ImageView ivCamera;
    TextView tvId;
    EditText etJob;
    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etAddress;
    EditText etHomepage;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivAvatar = findViewById(R.id.iv_avatar);
        ivCamera = findViewById(R.id.iv_camera);
        tvId = findViewById(R.id.tv_id);
        etJob = findViewById(R.id.et_job);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etHomepage = findViewById(R.id.et_homepage);
        btnSave = findViewById(R.id.btn_save);

        Intent intentSendFromMain = getIntent();
        Bundle extras = intentSendFromMain.getExtras();
        Object avatar = extras.get("avatar");
        if (avatar instanceof Bitmap) {
            ivAvatar.setImageBitmap((Bitmap) avatar);
        } else {
            ivAvatar.setImageResource((Integer) avatar);
        }
        tvId.setText(intentSendFromMain.getStringExtra("id"));
        etJob.setText(intentSendFromMain.getStringExtra("job"));
        etName.setText(intentSendFromMain.getStringExtra("name"));
        etPhone.setText(intentSendFromMain.getStringExtra("phone"));
        etEmail.setText(intentSendFromMain.getStringExtra("email"));
        etAddress.setText(intentSendFromMain.getStringExtra("address"));
        etHomepage.setText(intentSendFromMain.getStringExtra("homepage"));

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String name = etName.getText().toString();
                    String[] words = name.split(" ");
                    String id = words[0];
                    for (int i = 1; i < words.length; i++) {
                        id += "_" + words[i];
                    }
                    tvId.setText(id);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();

                if (ivAvatar.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.avatar).getConstantState()) {
                    resultIntent.putExtra("avatar", R.drawable.avatar);
                } else {
                    resultIntent.putExtra("avatar", ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap());
                }
                resultIntent.putExtra("id", tvId.getText().toString());
                resultIntent.putExtra("job", etJob.getText().toString());
                resultIntent.putExtra("name", etName.getText().toString());
                resultIntent.putExtra("phone", etPhone.getText().toString());
                resultIntent.putExtra("email", etEmail.getText().toString());
                resultIntent.putExtra("address", etAddress.getText().toString());
                resultIntent.putExtra("homepage", etHomepage.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(photo);
        }
    }
}