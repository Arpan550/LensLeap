package com.example.lensleap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lensleap.databinding.FragmentAddBinding;
import com.example.lensleap.ui.newposts.NewPostActivity;
import com.example.lensleap.ui.newposts.NewReelsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddFragment extends BottomSheetDialogFragment {
    FragmentAddBinding addBinding;
    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addBinding = FragmentAddBinding.inflate(inflater, container, false);
        return addBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBinding.addPostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewPostActivity.class));
            }
        });

        addBinding.addReelsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewReelsActivity.class));
            }
        });
    }
}
