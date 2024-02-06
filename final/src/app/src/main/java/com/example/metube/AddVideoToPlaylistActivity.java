package com.example.metube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.adapter.AddVideoToPlaylistAdapter;
import com.example.metube.model.Playlist;
import com.example.metube.model.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddVideoToPlaylistActivity extends AppCompatActivity implements AddVideoToPlaylistAdapter.OnVideoClickListener {
    ImageView iv_logo;
    RecyclerView rv;
    AddVideoToPlaylistAdapter adapter;
    private RecycleViewClickInterface recycleViewOnclick;
    private int selectedPosition;
    private CheckBox checkBoxSelected;
    private int selectedVideoCount = 0;
    private TextView tvSelectedVideo;
    private TextView tvAddVideoToPlaylistNext;
    private String namePlaylist = "";
    public String UserID ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // set adapter
    List<Video> videos = new ArrayList<>();
    List<String> videoIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_to_playlist);

        Intent intent = getIntent();
        UserID = intent.getStringExtra("userID");
        CreateItemList(videos);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList(List<Video> videos) {
        CreateClick();

        iv_logo = findViewById(R.id.iv_logo);
        rv = findViewById(R.id.rv_add_video_to_playlist);
        tvSelectedVideo = findViewById(R.id.tv_selected_video);
        tvAddVideoToPlaylistNext = findViewById(R.id.tv_add_video_to_playlist_next);

        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        CollectionReference videosCollection = firestore.collection("Videos");
        videosCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Video video = documentSnapshot.toObject(Video.class);
                        videos.add(video);
                    }
                }

                adapter = new AddVideoToPlaylistAdapter(AddVideoToPlaylistActivity.this, videos, recycleViewOnclick, AddVideoToPlaylistActivity.this);
                rv.setAdapter(adapter);
            }
        });



        tvAddVideoToPlaylistNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(AddVideoToPlaylistActivity.this);

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AddVideoToPlaylistActivity.this)
                    .setTitle("Playlist name")
                    .setView(input)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            namePlaylist = input.getText().toString();
                            Toast.makeText(AddVideoToPlaylistActivity.this, "Lưu playlist " + namePlaylist + " thành công", Toast.LENGTH_SHORT).show();
                            Playlist playlist = new Playlist(
                                    UserID,
                                    namePlaylist,
                                    true,
                                    Calendar.getInstance().getTime(),
                                    selectedVideoCount,
                                    "",
                                    videoIds
                            );

                            DocumentReference playlistRef = firestore.collection("Playlists").document();
                            playlistRef.set(playlist)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String playlistId = playlistRef.getId();
                                        playlist.setId(playlistId);
                                        playlistRef.set(playlist)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firestore", "Playlist added with ID: " + playlistId);
                                                    DocumentReference userRef = firestore.collection("Users").document(UserID);
                                                    userRef.get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if (documentSnapshot.exists()) {
                                                                    List<String> playlistIds = new ArrayList<>();
                                                                    playlistIds = (List<String>) documentSnapshot.get("playlists");
                                                                    playlistIds.add(playlistId);

                                                                    userRef.update("playlists", playlistIds)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d("Firestore", "Playlist added with ID: " + playlistId);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e("Firestore", "Error updating playlist", e);
                                                                            }
                                                                        });
                                                                }
                                                            }
                                                        });
                                                }
                                            })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("Firestore", "Error updating playlist", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firestore", "Error adding playlist", e);
                                    }
                                });
                            finish();
                        }
                    });

                AlertDialog dialog = materialAlertDialogBuilder.create();
                dialog.show();
            }
        });

        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void CreateClick() {
        recycleViewOnclick = new RecycleViewClickInterface() {
            @Override
            public void onRecycleViewClick(View view, int position) {
                CheckBox checkBox = view.findViewById(R.id.cb_add_video_to_playlist);
                boolean isChecked = checkBox.isChecked();

                if (isChecked) {
                    videoIds.add(videos.get(position).getId());
                    selectedVideoCount++;
                } else {
                    videoIds.remove(videos.get(position).getId());
                    selectedVideoCount--;
                }
                tvSelectedVideo.setText(String.format("Đã chọn %d video", selectedVideoCount));

            }

            @Override
            public void onRecycleViewLongClick(View view, int position) {
            }

        };
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(this, VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
    }
}