package com.example.ex4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 1;

    ImageView ivAvatar;
    ImageView ivEdit;
    TextView tvId;
    TextView tvJob;
    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvAddress;
    TextView tvHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAvatar = findViewById(R.id.iv_avatar);
        ivEdit = findViewById(R.id.iv_edit);
        tvId = findViewById(R.id.tv_id);
        tvJob = findViewById(R.id.tv_job);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvAddress = findViewById(R.id.tv_address);
        tvHomepage = findViewById(R.id.tv_homepage);

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);

                // first time: image get from resource, other time: image get from camera
                if (ivAvatar.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.avatar).getConstantState()) {
                    intent.putExtra("avatar", R.drawable.avatar);
                } else {
                    intent.putExtra("avatar", ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap());
                }
                intent.putExtra("id", tvId.getText().toString());
                intent.putExtra("job", tvJob.getText().toString());
                intent.putExtra("name", tvName.getText().toString());
                intent.putExtra("phone", tvPhone.getText().toString());
                intent.putExtra("email", tvEmail.getText().toString());
                intent.putExtra("address", tvAddress.getText().toString());
                intent.putExtra("homepage", tvHomepage.getText().toString());
                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Object avatar = extras.get("avatar");
            if (avatar instanceof Bitmap) {
                ivAvatar.setImageBitmap((Bitmap) avatar);
            } else {
                ivAvatar.setImageResource((Integer) avatar);
            }
            tvId.setText(data.getStringExtra("id"));
            tvJob.setText(data.getStringExtra("job"));
            tvName.setText(data.getStringExtra("name"));
            tvPhone.setText(data.getStringExtra("phone"));
            tvEmail.setText(data.getStringExtra("email"));
            tvAddress.setText(data.getStringExtra("address"));
            tvHomepage.setText(data.getStringExtra("homepage"));
        }
    }
}