package com.example.lensleap.ui.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lensleap.R;
import com.example.lensleap.adapters.MyPostAdapter;
import com.example.lensleap.databinding.FragmentPostBinding;
import com.example.lensleap.datamodel.MyPostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    FragmentPostBinding postBinding;
    ArrayList<MyPostModel> myPostModels;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        postBinding = FragmentPostBinding.inflate(inflater, container, false);
        return postBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myPostModels = new ArrayList<>();

        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));
        myPostModels.add(new MyPostModel(R.drawable.img));



        MyPostAdapter myPostAdapter = new MyPostAdapter(getContext(),myPostModels);
        postBinding.postReclyclerView.setAdapter(myPostAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        postBinding = null;
    }
}
