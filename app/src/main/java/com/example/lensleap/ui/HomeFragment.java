package com.example.lensleap.ui;

import static android.text.TextUtils.replace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lensleap.R;
import com.example.lensleap.adapters.PostAdapter;
import com.example.lensleap.adapters.StoryAdapter;
import com.example.lensleap.databinding.FragmentHomeBinding;
import com.example.lensleap.datamodel.PostModel;
import com.example.lensleap.datamodel.StoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding homebinding;
    private ArrayList<StoryModel> storyModels;
    private ArrayList<PostModel> postModels;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homebinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homebinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ArrayList
        storyModels = new ArrayList<>();
        postModels = new ArrayList<>();

        // Add items to the ArrayList
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));
        storyModels.add(new StoryModel("Arpan", R.drawable.img));

        // Set up the adapter for the horizontalRecyclerView
        homebinding.horizontalRecyclerView.setAdapter(new StoryAdapter(getContext(), storyModels));

        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));
        postModels.add(new PostModel("Arpan", R.drawable.img, R.drawable.img_placeholder));


        // Set up the adapter for the verticalRecyclerView
        homebinding.verticalRecyclerView.setAdapter(new PostAdapter(getContext(), postModels));


        homebinding.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new MassengerFragment());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homebinding = null;
    }

    public void addFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_home, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
