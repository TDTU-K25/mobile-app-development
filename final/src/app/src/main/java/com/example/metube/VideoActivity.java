package com.example.metube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.metube.adapter.ViewPagerAdapter;
import com.example.metube.fragment.EmptyFragment;
import com.example.metube.fragment.HeaderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VideoActivity extends AppCompatActivity {
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPref;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        sharedPref = getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = (sharedPref.getString(getString(R.string.saved_account_uid_key), ""));
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, userID);
        viewPager.setAdapter(viewPagerAdapter);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new HeaderFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_playlist) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_placeholder, new EmptyFragment()).commit();
                    viewPager.setCurrentItem(4);
                } else if (item.getItemId() == R.id.navigation_subscription) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_placeholder, new EmptyFragment()).commit();
                    viewPager.setCurrentItem(3);
                } else if (item.getItemId() == R.id.navigation_channel) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_placeholder, new EmptyFragment()).commit();
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.navigation_add) {
                    Intent intent = new Intent(VideoActivity.this, AddVideoActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.navigation_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new HeaderFragment()).commit();
                    viewPager.setCurrentItem(0);
                }
                return true;
            }
        });
    }
}