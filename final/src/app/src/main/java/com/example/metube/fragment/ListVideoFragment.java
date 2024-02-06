package com.example.metube.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metube.R;
import com.example.metube.VideoDetailActivity;
import com.example.metube.adapter.VideoAdapter;
import com.example.metube.model.Video;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ListVideoFragment extends Fragment implements VideoAdapter.OnVideoClickListener {
    final static String VIDEOS = "Videos";
    RecyclerView rvListVideo;
    VideoAdapter videoAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference videosRef = firebaseFirestore.collection(VIDEOS);

    public ListVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void setUpRecyclerView() {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListVideo.setLayoutManager(linearLayoutManager);

        Query query = videosRef;
        // simply get the data from query to the adapter
        FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>().setQuery(query, Video.class).build();
        videoAdapter = new VideoAdapter(options, getActivity(), this);
        rvListVideo.setAdapter(videoAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_video, container, false);
        rvListVideo = view.findViewById(R.id.rv_videos);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
    }

     @Override
     public void onStart() {
        super.onStart();
        videoAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoAdapter.stopListening();
    }

}