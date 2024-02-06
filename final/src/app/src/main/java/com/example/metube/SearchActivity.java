package com.example.metube;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metube.adapter.SearchVideoAdapter;
import com.example.metube.adapter.VideoAdapter;
import com.example.metube.model.Video;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {
    final static String VIDEOS = "Videos";
    SearchView searchView;
    RecyclerView rvSearchVideos;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference videosRef = firebaseFirestore.collection(VIDEOS);

    SearchVideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearchVideos = findViewById(R.id.rv_search_videos);
        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search for videos");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) { // When user presses search button
                videoAdapter.getFilter().filter(keyword);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                videoAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchVideos.setLayoutManager(linearLayoutManager);

        videosRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<Video> dataSource = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                Video data = document.toObject(Video.class);
                dataSource.add(data);
            }
            videoAdapter = new SearchVideoAdapter(this, dataSource, this);
            rvSearchVideos.setAdapter(videoAdapter);
        });
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(this, VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
        finish();
    }
}