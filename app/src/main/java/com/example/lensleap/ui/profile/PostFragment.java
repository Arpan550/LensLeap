package com.example.lensleap.ui.profile;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    FragmentPostBinding postBinding;
    ArrayList<MyPostModel> myPostModels;
    FirebaseFirestore db;
    MyPostAdapter myPostAdapter;
    ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        myPostModels = new ArrayList<MyPostModel>();
//
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//        myPostModels.add(new MyPostModel(R.drawable.img));
//

        db=FirebaseFirestore.getInstance();

        myPostAdapter = new MyPostAdapter(getContext(),myPostModels);
        postBinding.postReclyclerView.setAdapter(myPostAdapter);

        showPostListener();
    }

    private void showPostListener() {
        db.collection("userPosts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        // Clear the existing list to avoid duplicates
                        myPostModels.clear();

                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            MyPostModel myPostModel = snapshot.toObject(MyPostModel.class);
                            myPostModel.setImageUrl(snapshot.getString("imageUrl")); // Assuming "imageUrl" is the field name in Firestore
                            myPostModels.add(myPostModel);
                        }

                        myPostAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        postBinding = null;
    }
}
