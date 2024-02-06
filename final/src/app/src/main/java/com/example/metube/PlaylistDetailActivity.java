package com.example.metube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.adapter.AddVideoToPlaylistAdapter;
import com.example.metube.adapter.PlaylistAdapter;
import com.example.metube.adapter.VideoAdapter;
import com.example.metube.adapter.VideoListMinisizeAdapter;
import com.example.metube.fragment.EmptyFragment;
import com.example.metube.fragment.HeaderFragment;
import com.example.metube.model.Playlist;
import com.example.metube.model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Timer;


public class PlaylistDetailActivity extends AppCompatActivity implements VideoAdapter.OnVideoClickListener{
    RecyclerView rv;
    PlaylistAdapter adapter;
    VideoListMinisizeAdapter videoListMinisizeAdapter;
    ImageView iv_logo;
    BottomNavigationView bottomNavigationView;
    private RecycleViewClickInterface recycleViewOnclick;
    Button btnState;
    private int selectedPosition;
    private TextView title;
    private Button btnClearHistory;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    String userID;
    String playlistID;
    Boolean isMyOwnPlaylist = true;

    // set adapter
    List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_playlist);
        btnState = findViewById(R.id.btn_state);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                finish();
                return true;
            }
        });

        CreateItemList();

        Intent intent = getIntent();
        playlistID = intent.getStringExtra("playlistId");
        userID = intent.getStringExtra("userID");
        DocumentReference PlaylistRef = firestore.collection("Playlists").document(playlistID);

        PlaylistRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Playlist playlist = documentSnapshot.toObject(Playlist.class);
                if (!userID.equals(playlist.getUserID())) {
                    isMyOwnPlaylist = false;
                }
                if(playlist.getState()) {
                    btnState.setText("Công khai");
                } else {
                    btnState.setText("Riêng tư");
                }
                List<String> videoIDList = playlist.getVideos();
                final int[] videosProcessed = {0};

                for (String id : videoIDList) {
                    Log.d("VideoID", id);
                    firestore.collection("Videos").document(id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                videosProcessed[0]++;
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Video video = document.toObject(Video.class);
                                        videoList.add(video);

                                        if (videosProcessed[0] == videoIDList.size()) {
                                            createVideoList(null, videoList, false);
                                        }
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                    Toast.makeText(PlaylistDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(PlaylistDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList() {

        rv = findViewById(R.id.rv_playlist);
        title = findViewById(R.id.title_sub_library);

        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference PlaylistRef = firestore.collection("Playlists").document(playlistID);
                PlaylistRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Playlist playlist = documentSnapshot.toObject(Playlist.class);
                        if(!userID.equals(playlist.getUserID())) {
                            Toast.makeText(PlaylistDetailActivity.this, "Không thể thay đổi trạng thái playlist của người khác", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(playlist.getState()) {
                            btnState.setText("Riêng tư");
                            playlist.setState(false);
                        } else {
                            btnState.setText("Công khai");
                            playlist.setState(true);
                        }
                        PlaylistRef.set(playlist).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PlaylistDetailActivity.this, "Đã cập nhật trạng thái playlist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void createVideoList(List<Playlist> playlists, List<Video> videoList, boolean isPlaylist){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        CreateClick();

        videoListMinisizeAdapter = new VideoListMinisizeAdapter(this, videoList, this::onVideoClick, recycleViewOnclick, false);
        rv.setAdapter(videoListMinisizeAdapter);

    }

    private void CreateClick() {
        recycleViewOnclick = new RecycleViewClickInterface() {
            @Override
            public void onRecycleViewClick(View view, int position) {
                registerForContextMenu(view);
                openContextMenu(view);
                unregisterForContextMenu(view);
                selectedPosition = position;
            }

            @Override
            public void onRecycleViewLongClick(View view, int position) {
                Toast.makeText(PlaylistDetailActivity.this, "Long click: " + position, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_click_video_in_playlist, menu);
    }


    @Override
    @SuppressLint("NotifyDataSetChanged")
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            Toast.makeText(PlaylistDetailActivity.this, "Edit: " + selectedPosition, Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_delete) {
            videoList.remove(selectedPosition);
            List<String> videoListID = new ArrayList<>();
            for (Video video : videoList) {
                videoListID.add(video.getId());
            }

            if(!isMyOwnPlaylist) {
                Toast.makeText(PlaylistDetailActivity.this, "Không thể xóa video khỏi playlist của người khác", Toast.LENGTH_SHORT).show();
                return true;
            }

            firestore.collection("Playlists").document(playlistID).update("videos", videoListID)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            videoListMinisizeAdapter.notifyDataSetChanged();
                            Toast.makeText(PlaylistDetailActivity.this, "Đã xóa video khỏi playlist", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlaylistDetailActivity.this, "Xóa video khỏi playlist thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });

            videoListMinisizeAdapter.notifyDataSetChanged();
        }
        return true;
    };

    @Override
    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(this, VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
    }
}