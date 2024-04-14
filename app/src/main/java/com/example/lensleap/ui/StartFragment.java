package com.example.lensleap.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lensleap.R;
import com.example.lensleap.adapters.MessengerViewPagerAdapter;
import com.example.lensleap.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
    FragmentStartBinding startBinding;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startBinding=FragmentStartBinding.inflate(inflater,container,false);
        return startBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MessengerViewPagerAdapter adapter=new MessengerViewPagerAdapter(getChildFragmentManager());
        startBinding.startViewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        startBinding=null;
    }
}