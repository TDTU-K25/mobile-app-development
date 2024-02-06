package com.example.metube;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metube.adapter.VideoManagementAdapter;
import com.example.metube.model.Video;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VideoManagementActivity extends AppCompatActivity {
    RecyclerView recyclerViewVideoManagement;
    VideoManagementAdapter videoManagementAdapter;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_management);

        recyclerViewVideoManagement = findViewById(R.id.rv_videos_management);

        // set layout managet for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewVideoManagement.setLayoutManager(linearLayoutManager);

        // set adapter
        Query query = firestore.collection("Videos");
        FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>().setQuery(query, Video.class).build();
        videoManagementAdapter = new VideoManagementAdapter(options, this);
        recyclerViewVideoManagement.setAdapter(videoManagementAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoManagementAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoManagementAdapter.stopListening();
    }

}