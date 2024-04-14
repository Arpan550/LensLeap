package com.example.lensleap.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lensleap.LogInActivity;
import com.example.lensleap.R;
import com.example.lensleap.SettingsActivity;
import com.example.lensleap.adapters.ProfileUserViewPagerAdapter;
import com.example.lensleap.databinding.FragmentProfileUserBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileUserFragment extends Fragment {
    FragmentProfileUserBinding profileUserBinding;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    StorageReference storageReference;

    public ProfileUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileUserBinding = FragmentProfileUserBinding.inflate(inflater, container, false);
        return profileUserBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase components
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (currentUser != null) {
            // Load profile image from Firebase Storage
            loadProfileImage();

            // Set email directly from FirebaseUser
            profileUserBinding.textViewEmail.setText(currentUser.getEmail());

            // Fetch and set username from Firestore
            fetchUsername();
        }

        // log out handle
        profileUserBinding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LogInActivity.class));
                requireActivity().finish();
            }
        });

        // go to edit profile
        profileUserBinding.buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Set the tab layout
        ProfileUserViewPagerAdapter profileUserViewPagerAdapter=new ProfileUserViewPagerAdapter(getChildFragmentManager());
        profileUserBinding.viewPager.setAdapter(profileUserViewPagerAdapter);
        profileUserBinding.tabLayout.setupWithViewPager(profileUserBinding.viewPager);
    }

    private void loadProfileImage() {
        // Show placeholder while loading
        profileUserBinding.dpImage.setImageResource(R.drawable.img_placeholder);

        StorageReference profileImageRef = storageReference.child("users/" + currentUser.getUid() + "/profile.jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.img_placeholder).into(profileUserBinding.dpImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LoadProfileImage", "Failed to load profile image", e);
                Toast.makeText(requireContext(), "Failed to load profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsername() {
        // Show placeholder username while loading
        profileUserBinding.textViewUsername.setText("Loading...");

        db.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String username = document.getString("username");
                            profileUserBinding.textViewUsername.setText(username);
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profileUserBinding = null;
    }
}

