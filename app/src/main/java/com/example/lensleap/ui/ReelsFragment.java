package com.example.lensleap.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lensleap.R;
import com.example.lensleap.adapters.ReelsAdapter;
import com.example.lensleap.datamodel.ReelsModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


@UnstableApi public class ReelsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReelsAdapter reelsAdapter;
    private ArrayList<ReelsModel> reelsModels;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    private SimpleExoPlayer exoPlayer;

    public ReelsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reels, container, false);
        recyclerView = view.findViewById(R.id.postReclyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reelsModels = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        reelsAdapter = new ReelsAdapter(getContext(), reelsModels, exoPlayer);
        recyclerView.setAdapter(reelsAdapter);

        fetchReelsData();
    }

    private void fetchReelsData() {
        db.collection("userReels")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ReelsModel reelsModel = snapshot.toObject(ReelsModel.class);
                        if (reelsModel != null) {
                            reelsModel.setReels_post(snapshot.getString("videoUrl")); // Assuming "videoUrl" is the field name in Firestore
                            reelsModel.setCaption(snapshot.getString("caption"));
                            reelsModel.setUid(snapshot.getString("userID")); // Assuming "uid" is the user ID field
                            fetchUserDetails(reelsModel);
                        }
                    }
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e("ReelsFragment", "Error fetching reels data: " + e.getMessage());
                });
    }

    private void fetchUserDetails(ReelsModel reelsModel) {
        db.collection("users")
                .document(reelsModel.getUid()) // Get user document using user ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User document found, fetch username
                        String username = documentSnapshot.getString("username");
                        reelsModel.setUsername(username);

                        // Fetch profile image URL from Firebase Storage
                        fetchProfileImageUrl(reelsModel);
                    } else {
                        Log.e("ReelsFragment", "User document not found for user ID: " + reelsModel.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ReelsFragment", "Error fetching user details: " + e.getMessage());
                });
    }

    private void fetchProfileImageUrl(ReelsModel reelsModel) {
        // Get reference to the profile image in Firebase Storage
        StorageReference profileImageRef = storage.getReference()
                .child("users")
                .child(reelsModel.getUid()) // UID folder
                .child("profile.jpg"); // Assuming the profile image name is "profile.jpg"

        // Get the download URL for the profile image
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Profile image URL fetched successfully
                    reelsModel.setProfile_img(uri.toString());

                    // Add the updated reelsModel to the list
                    reelsModels.add(reelsModel);
                    reelsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("ReelsFragment", "Error fetching profile image URL: " + e.getMessage());
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
