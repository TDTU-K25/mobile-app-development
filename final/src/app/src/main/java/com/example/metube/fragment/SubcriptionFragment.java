package com.example.metube.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.metube.AddVideoToPlaylistActivity;
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.adapter.ChannelAdapter;
import com.example.metube.adapter.PlaylistAdapter;
import com.example.metube.model.Playlist;
import com.example.metube.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SubcriptionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    SharedPreferences sharedPref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID;
    DocumentReference userRef;

    private RecycleViewClickInterface recycleViewOnclick;
    private int selectedPosition;
    RecyclerView rvPlaylist;
    ChannelAdapter channelAdapter;
    List<User> userList = new ArrayList<>();


    public SubcriptionFragment() {
        // Required empty public constructor
    }

    public static SubcriptionFragment newInstance(String param1, String param2) {
        SubcriptionFragment fragment = new SubcriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreateClick();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = (sharedPref.getString(getString(R.string.saved_account_uid_key), ""));
        userRef = firestore.collection("Users").document(userID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subcription, container, false);
        rvPlaylist = view.findViewById(R.id.rv_playlist);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            userList.clear();
            List<String> userSubscribedChannels = user.getSubscribedChannels();

            for (String channelID : userSubscribedChannels) {
                firestore.collection("Users").document(channelID).get().addOnSuccessListener(documentSnapshot1 -> {
                    User channel = documentSnapshot1.toObject(User.class);
                    userList.add(channel);
                    CreateItemList(userList);
                });
            }
        });
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList(List<User> userList) {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlaylist.setLayoutManager(linearLayoutManager);

        channelAdapter = new ChannelAdapter(getActivity(), userList, recycleViewOnclick);
        rvPlaylist.setAdapter(channelAdapter);
    }

    private void CreateClick() {
        recycleViewOnclick = new RecycleViewClickInterface() {
            @Override
            public void onRecycleViewClick(View view, int position) {
                // Hiển thị ContextMenu
                registerForContextMenu(view);
                getActivity().openContextMenu(view);
                unregisterForContextMenu(view);

                // Lưu trữ vị trí được chọn
                selectedPosition = position;
            }

            @Override
            public void onRecycleViewLongClick(View view, int position) {
            }
        };
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_click_video_in_playlist, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.view_pager, new ChannelFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }
        else
        if (id == R.id.menu_delete) {
            userList.remove(selectedPosition);
            List<String> userIDList = new ArrayList<>();
            for (User user : userList) {
                userIDList.add(user.getId());
            }
            userRef.update("subscribedChannels", userIDList)
                .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Đã huỷ đăng ký", Toast.LENGTH_SHORT).show();
                        channelAdapter.notifyItemRemoved(selectedPosition);
                        channelAdapter.notifyDataSetChanged();
                    }
                )
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Lỗi! Không thể huỷ đăng ký", Toast.LENGTH_SHORT).show());
            return true;
        }
        return super.onContextItemSelected(item);
    }
}