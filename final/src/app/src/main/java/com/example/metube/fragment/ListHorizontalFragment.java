package com.example.metube.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.metube.R;
import com.example.metube.VideoDetailActivity;
import com.example.metube.adapter.VideoAdapter;
import com.example.metube.adapter.VideoListMinisizeAdapter;
import com.example.metube.model.Playlist;
import com.example.metube.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListHorizontalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListHorizontalFragment extends Fragment  implements VideoAdapter.OnVideoClickListener{

    RecyclerView rvListVideo;
    VideoListMinisizeAdapter videoListMinisizeAdapter;
    private List<Video> videos = new ArrayList<>();
    private List<Playlist> playlists = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListHorizontalFragment() {
        // Required empty public constructor
    }

    public ListHorizontalFragment(List<Video> videos) {
        this.videos.addAll(videos);
    }

    public ListHorizontalFragment(List<Playlist> playlists, int i) {
        this.playlists.addAll(playlists);
    }

    public static ListHorizontalFragment newInstance(String param1, String param2) {
        ListHorizontalFragment fragment = new ListHorizontalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_horizontal, container, false);
        rvListVideo = view.findViewById(R.id.rv_videos_horizontal);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvListVideo.setLayoutManager(linearLayoutManager);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvListVideo);

        videoListMinisizeAdapter = new VideoListMinisizeAdapter(getActivity(), videos, this::onVideoClick,true);
        rvListVideo.setAdapter(videoListMinisizeAdapter);
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
    }
}