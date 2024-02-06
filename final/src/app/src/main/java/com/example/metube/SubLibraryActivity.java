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


public class SubLibraryActivity extends AppCompatActivity implements VideoAdapter.OnVideoClickListener{
    RecyclerView rv;
    PlaylistAdapter adapter;
    VideoListMinisizeAdapter videoListMinisizeAdapter;
    ImageView iv_logo;
    BottomNavigationView bottomNavigationView;
    private RecycleViewClickInterface recycleViewOnclick;
    private int selectedPosition;
    private Button btn_Add_Playlist;
    private TextView title;
    private Button btnClearHistory;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    String userID;

    // set adapter
    List<Playlist> playlists = new ArrayList<>();
    List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_library);

        sharedPref = getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = (sharedPref.getString(getString(R.string.saved_account_uid_key), ""));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_playlist);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                finish();
                return true;
            }
        });

        CreateItemList();

        Intent intent = getIntent();
        String title_Intent = intent.getStringExtra("title");
        DocumentReference userRef = firestore.collection("Users").document(userID);

        if(title_Intent.equals("playlist")) {

            title.setText("Danh sách phát");
            btnClearHistory.setVisibility(View.GONE);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> likedVideos = (List<String>) document.get("playlists");
                            if (likedVideos != null) {
                                for (String videoId : likedVideos) {
                                    CollectionReference videosCollection = firestore.collection("Playlists");
                                    videosCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                if (documentSnapshot.exists()) {
                                                    Playlist playlist = documentSnapshot.toObject(Playlist.class);
                                                    if(! ( (playlist.getState() == false) && (!playlist.getUserID().equals(userID)))){
                                                        playlists.add(playlist);
                                                    }
                                                }
                                            }
                                            createVideoList(playlists, null, true);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SubLibraryActivity.this, "No liked videos found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubLibraryActivity.this, "No user found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SubLibraryActivity.this, "Error getting user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if(title_Intent.equals("history")) {
            title.setText("Lịch sử xem");
            btn_Add_Playlist.setVisibility(View.GONE);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> watchedVideos = (List<String>) document.get("watched");
                            if (watchedVideos != null) {
                                for (String videoId : watchedVideos) {
                                    DocumentReference videoRef = firestore.collection("Videos").document(videoId);
                                    videoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Video videos = new Video(
                                                            document.getId(),
                                                            document.getString("title"),
                                                            document.getLong("views").intValue(),
                                                            new Date(2023, 10, 10),
                                                            new Date(2023, 10, 10),
                                                            document.getString("url"),
                                                            document.getString("thumbnail")
                                                    );

                                                    videoList.add(videos);
                                                    createVideoList(null, videoList, false);
                                                } else {
                                                    Toast.makeText(SubLibraryActivity.this, "No video found.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SubLibraryActivity.this, "Error getting video.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SubLibraryActivity.this, "No liked videos found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubLibraryActivity.this, "No user found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SubLibraryActivity.this, "Error getting user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if(title_Intent.equals("favorite")) {
            title.setText("Danh sách yêu thích");
            btn_Add_Playlist.setVisibility(View.GONE);
            btnClearHistory.setVisibility(View.GONE);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> likedVideos = (List<String>) document.get("likedVideos");
                            if (likedVideos != null) {
                                for (String videoId : likedVideos) {
                                    DocumentReference videoRef = firestore.collection("Videos").document(videoId);
                                    videoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Video videos = new Video(
                                                        document.getId(),
                                                        document.getString("title"),
                                                        document.getLong("views").intValue(),
                                                        new Date(2023, 10, 10),
                                                        new Date(2023, 10, 10),
                                                        document.getString("url"),
                                                        document.getString("thumbnail")
                                                    );

                                                    videoList.add(videos);
                                                    createVideoList(null, videoList, false);
                                                } else {
                                                    Toast.makeText(SubLibraryActivity.this, "No video found.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SubLibraryActivity.this, "Error getting video.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SubLibraryActivity.this, "No liked videos found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubLibraryActivity.this, "No user found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SubLibraryActivity.this, "Error getting user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList() {

        rv = findViewById(R.id.rv_playlist);
        title = findViewById(R.id.title_sub_library);
        btnClearHistory = findViewById(R.id.btn_clear_history);
        btn_Add_Playlist = findViewById(R.id.btn_add_playlist);

        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Add_Playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubLibraryActivity.this, AddVideoToPlaylistActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference userRef = firestore.collection("Users").document(userID);
                userRef.update("watched", new ArrayList<String>())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SubLibraryActivity.this, "Đã xóa lịch sử xem", Toast.LENGTH_SHORT).show();
                            videoList.clear();
                            videoListMinisizeAdapter.notifyDataSetChanged();
                        }
                    }
                );
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void createVideoList(List<Playlist> playlists, List<Video> videoList, boolean isPlaylist){
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        CreateClick();

        if(isPlaylist) {
            adapter = new PlaylistAdapter(this, playlists, recycleViewOnclick);
            rv.setAdapter(adapter);
        } else {
            videoListMinisizeAdapter = new VideoListMinisizeAdapter(this, videoList, this::onVideoClick, recycleViewOnclick, false);
            rv.setAdapter(videoListMinisizeAdapter);
        }

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
                Toast.makeText(SubLibraryActivity.this, "Long click: " + position, Toast.LENGTH_SHORT).show();
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
        if (id == R.id.menu_edit){
            if(title.getText().equals("Danh sách phát")) {
                Intent intent = new Intent(SubLibraryActivity.this, PlaylistDetailActivity.class);
                intent.putExtra("playlistId", playlists.get(selectedPosition).getId());
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        }

        if(id == R.id.menu_delete ){

            if(title.getText().equals("Danh sách phát")) {
                if(!userID.equals(playlists.get(selectedPosition).getUserID())) {
                    Toast.makeText(SubLibraryActivity.this, "Bạn không thể xóa danh sách phát của người khác", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Playlist playlist = playlists.get(selectedPosition);
                playlists.remove(selectedPosition);
                adapter.notifyDataSetChanged();

                firestore.collection("Playlists").document(playlist.getId()).delete();
            } else if(title.getText().equals("Lịch sử xem")) {
                Video video = videoList.get(selectedPosition);
                videoList.remove(selectedPosition);
                videoListMinisizeAdapter.notifyDataSetChanged();

                DocumentReference userRef = firestore.collection("Users").document(userID);
                userRef.update("watched", videoList)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                videoListMinisizeAdapter.notifyDataSetChanged();
                                Toast.makeText(SubLibraryActivity.this, "Đã xóa video khỏi lịch sử xem", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else if(title.getText().equals("Danh sách yêu thích")) {
                Video video = videoList.get(selectedPosition);
                videoList.remove(selectedPosition);
                videoListMinisizeAdapter.notifyDataSetChanged();

                DocumentReference userRef = firestore.collection("Users").document(userID);
                ArrayList<String> videoIdList = new ArrayList<>();
                for (Video vid: videoList) {
                    videoIdList.add(vid.getId());
                }
                userRef.update("likedVideos", videoIdList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        videoListMinisizeAdapter.notifyDataSetChanged();
                        Toast.makeText(SubLibraryActivity.this, "Đã xóa video khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return true;

    }

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