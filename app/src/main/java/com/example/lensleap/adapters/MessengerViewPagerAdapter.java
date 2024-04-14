package com.example.lensleap.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lensleap.ui.HomeFragment;
import com.example.lensleap.ui.MassengerFragment;

public class MessengerViewPagerAdapter extends FragmentPagerAdapter {

    public MessengerViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else {
            return new MassengerFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
