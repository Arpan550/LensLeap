package com.example.lensleap.ui.newposts;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lensleap.MainActivity;
import com.example.lensleap.databinding.ActivityNewPostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {
    ActivityNewPostBinding newPostBinding;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int PICK_IMAGE_REQUEST = 103;
    private Uri imageUri;
    FirebaseFirestore firestore;
    String userID;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newPostBinding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(newPostBinding.getRoot());

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...Uploading..");
        progressDialog.setCancelable(false);

        // Set up image picker dialog
        newPostBinding.imagePost.setOnClickListener(v -> showImagePickerDialog());

        // Set up post button click listener
        newPostBinding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog
                progressDialog.show();
                String caption = newPostBinding.editTextCaption.getText().toString();
                userID = auth.getCurrentUser().getUid();

                // Reference the collection where you want to add the new document
                CollectionReference collectionReference = firestore.collection("userPosts");

                Map<String, Object> userPosts = new HashMap<>();
                userPosts.put("caption", caption);
                userPosts.put("imageUrl", imageUri.toString());

                // Use add() to add a new document to the collection
                collectionReference.add(userPosts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Hide progress dialog
                        progressDialog.dismiss();
                        startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                        finish();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // Handle failure
                    }
                });
            }
        });

        // set up cancel button functionality
        newPostBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(new CharSequence[]{"Take photo", "Choose from gallery"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    checkCameraPermissionAndTakePhoto();
                    break;
                case 1:
                    choosePhotoFromGallery();
                    break;
            }
        });
        builder.show();
    }

    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhotoFromCamera();
        }
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    newPostBinding.imagePost.setImageBitmap(imageBitmap);
                    imageUri = getImageUri(imageBitmap);
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    newPostBinding.imagePost.setImageBitmap(bitmap);
                    imageUri = selectedImageUri;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        // Implement the conversion from Bitmap to Uri
        return null;
    }
}
