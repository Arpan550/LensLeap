package com.example.lensleap.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lensleap.R;
import com.example.lensleap.adapters.PostAdapter;
import com.example.lensleap.adapters.SearchPostAdapter;
import com.example.lensleap.databinding.FragmentHomeBinding;
import com.example.lensleap.databinding.FragmentSearchBinding;
import com.example.lensleap.datamodel.MyPostModel;
import com.example.lensleap.datamodel.PostModel;
import com.example.lensleap.datamodel.StoryModel;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding searchBinding;
    private ArrayList<PostModel> posts;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchBinding=FragmentSearchBinding.inflate(inflater, container, false);
        return searchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posts=new ArrayList<>();


        searchBinding.recyclerViewRVsearchFragment.setAdapter(new SearchPostAdapter(getContext(), posts));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchBinding=null;
    }
}