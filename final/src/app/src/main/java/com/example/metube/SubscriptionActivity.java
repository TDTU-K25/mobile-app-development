package com.example.metube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.adapter.ChannelAdapter;
import com.example.metube.fragment.ChannelFragment;
import com.example.metube.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID;
    DocumentReference userRef;

    private RecycleViewClickInterface recycleViewOnclick;
    private int selectedPosition;
    RecyclerView rvPlaylist;
    ChannelAdapter channelAdapter;
    List<User> userList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        rvPlaylist = findViewById(R.id.rv_playlist);

        CreateClick();

        sharedPref = getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = (sharedPref.getString(getString(R.string.saved_account_uid_key), ""));
        userRef = firestore.collection("Users").document(userID);

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

    }

    @SuppressLint("NotifyDataSetChanged")
    public void CreateItemList(List<User> userList) {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPlaylist.setLayoutManager(linearLayoutManager);

        channelAdapter = new ChannelAdapter(this, userList, recycleViewOnclick);
        rvPlaylist.setAdapter(channelAdapter);
    }

    private void CreateClick() {
        recycleViewOnclick = new RecycleViewClickInterface() {
            @Override
            public void onRecycleViewClick(View view, int position) {
                // Hiển thị ContextMenu
                registerForContextMenu(view);
                openContextMenu(view);
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
        getMenuInflater().inflate(R.menu.context_menu_click_video_in_playlist, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            FragmentManager fragmentManager = getSupportFragmentManager();
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
                                Toast.makeText(this, "Đã huỷ đăng ký", Toast.LENGTH_SHORT).show();
                                channelAdapter.notifyItemRemoved(selectedPosition);
                                channelAdapter.notifyDataSetChanged();
                            }
                    )
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi! Không thể huỷ đăng ký", Toast.LENGTH_SHORT).show());
            return true;
        }
        return super.onContextItemSelected(item);
    }
}