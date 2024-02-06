package com.example.metube.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.metube.R;
import com.example.metube.adapter.SearchVideoAdapter;
import com.example.metube.adapter.ShortAdapter;
import com.example.metube.model.Video;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShortFragment extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ViewPager2 viewPager2;
    private List<Video> videoList;
    private ShortAdapter shortsAdapter;


    public ShortFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_short, container, false);
        videoList = new ArrayList<>();
        viewPager2 = rootView.findViewById(R.id.viewPager2);

        firestore.collection("Videos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<Video> dataSource = new ArrayList<>();

            for (DocumentSnapshot document : documents) {
                Video data = document.toObject(Video.class);
                dataSource.add(data);
            }
            shortsAdapter = new ShortAdapter(getContext(), dataSource);
            viewPager2.setAdapter(shortsAdapter);
        });

        return rootView;
    }


}