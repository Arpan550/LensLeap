package com.example.lensleap.ui;

import static android.text.TextUtils.replace;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding homebinding;
    private ArrayList<StoryModel> storyModels;
    private ArrayList<PostModel> postModels;
    private PostAdapter postAdapter;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

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

        // Initialize the ArrayLists
        storyModels = new ArrayList<>();
        postModels = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Set up the adapter for the horizontalRecyclerView
        homebinding.horizontalRecyclerView.setAdapter(new StoryAdapter(getContext(), storyModels));

        db = FirebaseFirestore.getInstance();
        postAdapter = new PostAdapter(getContext(), postModels);
        homebinding.verticalRecyclerView.setAdapter(postAdapter);

        showPostListener();

        homebinding.message.setOnClickListener(v -> addFragment(new MassengerFragment()));
    }

    private void showPostListener() {
        db.collection("userPosts")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Log.e("Firestore Error", error.getMessage());
                        return;
                    }

                    // Clear the existing list to avoid duplicates
                    postModels.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        PostModel postModel = snapshot.toObject(PostModel.class);
                        postModel.setPost_img(snapshot.getString("imageUrl")); // Assuming "imageUrl" is the field name in Firestore
                        postModel.setCaption(snapshot.getString("caption"));
                        postModel.setUid(snapshot.getString("userID")); // Assuming "uid" is the user ID field
                        fetchUserDetails(postModel);
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void fetchUserDetails(PostModel postModel) {
        db.collection("users")
                .document(postModel.getUid()) // Get user document using user ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User document found, fetch username
                        String username = documentSnapshot.getString("username");
                        postModel.setUsername(username);

                        // Fetch profile image URL from Firebase Storage
                        fetchProfileImageUrl(postModel);
                    } else {
                        Log.e("HomeFragment", "User document not found for user ID: " + postModel.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeFragment", "Error fetching user details: " + e.getMessage());
                });
    }

    private void fetchProfileImageUrl(PostModel postModel) {
        // Get reference to the profile image in Firebase Storage
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(postModel.getUid()) // UID folder
                .child("profile.jpg"); // Assuming the profile image name is "profile.jpg"

        // Get the download URL for the profile image
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Profile image URL fetched successfully
                    postModel.setProfile_img(uri.toString());

                    // Add the updated post model to the list
                    postModels.add(postModel);
                    postAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeFragment", "Error fetching profile image URL: " + e.getMessage());
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