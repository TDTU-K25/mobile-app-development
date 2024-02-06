package com.example.metube.adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.metube.fragment.ChannelFragment;
import com.example.metube.fragment.LibraryFragment;
import com.example.metube.fragment.ListVideoFragment;
import com.example.metube.fragment.PlaylistFragment;
import com.example.metube.fragment.ShortFragment;
import com.example.metube.fragment.SubcriptionFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String userID;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String userID) {
        super(fm, behavior);
        this.userID = userID;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new ListVideoFragment();
        } else if (i == 1) {
            return new ChannelFragment();
        } else if (i == 4) {
            return new LibraryFragment();
        } else if (i == 3) {
            return new ShortFragment();
        }
        return new ListVideoFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }
}