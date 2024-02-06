package com.example.metube.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.metube.EditChannelActivity;
import com.example.metube.R;
import com.example.metube.VideoManagementActivity;
import com.example.metube.adapter.ChannelAdapter;
import com.example.metube.adapter.ChannelViewPagerAdapter;
import com.example.metube.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChannelFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    View view;
    Button btnManageVideos;
    ImageView ivAvatarChannel;
    TextView tvChannelName;
    TextView tvChannelId;
    View viewHeaderChannel;
    TextView tvHeaderChannelName;
    ImageView ivEditChannel;

    String userID;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ChannelFragment() {
    }

    public ChannelFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        tabLayout = view.findViewById(R.id.tab_layout_channel);
        viewPager = view.findViewById(R.id.view_pager_channel);
        ivAvatarChannel = view.findViewById(R.id.iv_avatar_channel);
        tvChannelName = view.findViewById(R.id.tv_channel_name);
        tvChannelId = view.findViewById(R.id.tv_channel_id);
        viewHeaderChannel = view.findViewById(R.id.header_channel);
        ivEditChannel = view.findViewById(R.id.iv_edit_channel);
        tvHeaderChannelName = viewHeaderChannel.findViewById(R.id.tv_header_channel_name);


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_login_key), Context.MODE_PRIVATE);
        userID = sharedPref.getString(getString(R.string.saved_account_uid_key), "");

        ChannelViewPagerAdapter channelViewPagerAdapter = new ChannelViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(channelViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tvChannelName = view.findViewById(R.id.tv_channel_name);

        btnManageVideos = view.findViewById(R.id.btn_manage_videos);
        btnManageVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoManagementActivity.class);
                startActivity(intent);
            }
        });

        ivEditChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditChannelActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        firestore.collection(getString(R.string.users_collection)).document(userID).get().addOnSuccessListener(documentSnapshot -> {
            tvChannelId.setText(userID);
            String name = documentSnapshot.getString("name");
            tvChannelName.setText(name);
            tvHeaderChannelName.setText(name);
            Glide.with(getContext()).load(documentSnapshot.getString("avatar")).into(ivAvatarChannel);
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}