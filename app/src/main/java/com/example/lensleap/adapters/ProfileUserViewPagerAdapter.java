package com.example.lensleap.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lensleap.ui.profile.MyReelsFragment;
import com.example.lensleap.ui.profile.PostFragment;

public class ProfileUserViewPagerAdapter extends FragmentPagerAdapter {
    public ProfileUserViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PostFragment();
        } else{
            return new MyReelsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "My Posts";
        } else {
            return "My Reels";
        }
    }
}
