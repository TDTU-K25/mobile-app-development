package com.example.metube;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.metube.model.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

public class AddVideoActivity extends AppCompatActivity {
    final static int REQUEST_CODE_SELECT_VIDEO_FROM_GALLERY = 1;
    final static int REQUEST_CODE_SELECT_VIDEO_FROM_CAMERA = 2;
    FloatingActionButton fabChooseVideo;
    EditText etTitle;
    EditText etDescription;
    VideoView vvDemoBeforeUpload;
    Uri videoUri;
    Button btnUploadVideo;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    String userID; // channel id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        vvDemoBeforeUpload = findViewById(R.id.vv_demo_before_upload);
        fabChooseVideo = findViewById(R.id.fab_choose_video);
        btnUploadVideo = findViewById(R.id.btn_upload_video);

        // get user id
        this.sharedPref = getSharedPreferences(getString(R.string.preference_login_key), Context.MODE_PRIVATE);
        userID = sharedPref.getString(getString(R.string.saved_account_uid_key), "");

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "videos/" + System.currentTimeMillis() + ".mp4";
                StorageReference storageReference = firebaseStorage.getReference(location);
                storageReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // then save information to firestore
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ; // wait until uriTask is successful
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()) {
                            // save to firestore
                            String title = etTitle.getText().toString();
                            String description = etDescription.getText().toString();
                            try {
                                Video video = new Video(title, 0, new Date(System.currentTimeMillis()), getDuration(videoUri), downloadUri.toString(), description, userID);
                                firestore.collection("Videos").add(video).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        documentReference.update("id", documentReference.getId());
                                        Toast.makeText(AddVideoActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddVideoActivity.this, VideoActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddVideoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddVideoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fabChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseVideoDialog();
            }
        });
    }

    private Date getDuration(Uri uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);

        retriever.release();
        return new Date(timeInMillisec);
    }

    private void showChooseVideoDialog() {
        String[] options = new String[]{"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Video From").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { // Camera
                    chooseVideoFromCamera();
                } else { // Gallery
                    chooseVideoFromGallery();
                }
            }
        }).show();
    }

    private void chooseVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_CODE_SELECT_VIDEO_FROM_GALLERY);
    }

    private void chooseVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_VIDEO_FROM_CAMERA) {
                videoUri = data.getData();
                setVideoForVideoView();
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO_FROM_GALLERY) {
                videoUri = data.getData();
                setVideoForVideoView();
            }
        }
    }

    private void setVideoForVideoView() {
        vvDemoBeforeUpload.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vvDemoBeforeUpload);
        vvDemoBeforeUpload.setMediaController(mediaController);
        vvDemoBeforeUpload.requestFocus();
    }
}