package com.example.metube.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metube.AddVideoToPlaylistActivity;
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.adapter.PlaylistAdapter;
import com.example.metube.model.Playlist;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rvPlaylist;
    PlaylistAdapter playlistAdapter;
    // set adapter
    List<Playlist> playlists = new ArrayList<>();
    private RecycleViewClickInterface recycleViewOnclick;
    private int selectedPosition;
    private ImageView ivAddPlaylist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        rvPlaylist = view.findViewById(R.id.rv_playlist);
        ivAddPlaylist = view.findViewById(R.id.iv_add_playlist);
        CreateItemList(playlists);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList(List<Playlist> playlists) {


        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlaylist.setLayoutManager(linearLayoutManager);



        playlistAdapter = new PlaylistAdapter(getActivity(), playlists, recycleViewOnclick);
        rvPlaylist.setAdapter(playlistAdapter);

        CreateClick();
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

        ivAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddVideoToPlaylistActivity.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_click_video_in_playlist, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_delete) {
            Toast.makeText(getActivity(), "Delete: " + selectedPosition, Toast.LENGTH_SHORT).show();
            playlists.remove(selectedPosition);
            playlistAdapter.notifyDataSetChanged();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String title = getActivity().getIntent().getStringExtra("name");
        String createAt = getActivity().getIntent().getStringExtra("createAt");
        String updateAt = getActivity().getIntent().getStringExtra("updateAt");
        Integer numOfVideo = getActivity().getIntent().getIntExtra("numOfVideo", 0);

//        playlists.add(new Playlist(title, true, new Date(2023, 10, 10), new Date(2023, 11, 10), numOfVideo, "https://www.techsmith.com/blog/wp-content/uploads/2021/02/TSC-thumbnail-example-1024x576.png"));

        playlistAdapter.notifyDataSetChanged();
    }
}