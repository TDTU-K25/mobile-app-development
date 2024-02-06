package com.example.metube.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.metube.fragment.channel.HomeChannelFragment;
import com.example.metube.fragment.channel.PlaylistChannelFragment;

public class ChannelViewPagerAdapter extends FragmentStatePagerAdapter {
    public ChannelViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new HomeChannelFragment();
        } else if (i == 1) {
            return new PlaylistChannelFragment();
        } else {
            return new HomeChannelFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Playlists";
            default:
                return "Home";
        }
    }
}