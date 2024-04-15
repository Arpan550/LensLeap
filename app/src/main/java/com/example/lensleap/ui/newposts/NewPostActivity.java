package com.example.lensleap.ui.newposts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newPostBinding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(newPostBinding.getRoot());

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("post_images");

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

                // Upload image to Firebase Storage
                uploadImage();
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

    // Method to show image picker dialog
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

    // Method to check camera permission and take photo
    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhotoFromCamera();
        }
    }

    // Method to take photo from camera
    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Method to choose photo from gallery
    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method to upload image to Firebase Storage
    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save caption and image URL to Firestore
                            saveToFirestore(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                        progressDialog.dismiss();
                        Toast.makeText(NewPostActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save caption and image URL to Firestore
    private void saveToFirestore(String imageUrl) {
        String caption = newPostBinding.editTextCaption.getText().toString();
        String userID = auth.getCurrentUser().getUid();

        // Reference the collection where you want to add the new document
        CollectionReference collectionReference = firestore.collection("userPosts");

        Map<String, Object> userPosts = new HashMap<>();
        userPosts.put("caption", caption);
        userPosts.put("imageUrl", imageUrl);
        userPosts.put("userID", userID);

        // Use add() to add a new document to the collection
        collectionReference.add(userPosts).addOnSuccessListener(documentReference -> {
            // Hide progress dialog
            progressDialog.dismiss();
            startActivity(new Intent(NewPostActivity.this, MainActivity.class));
            finish();
            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            //Log.w(TAG, "Error adding document", e);
            // Handle failure
        });
    }

    // Method to get file extension from image URI
    private String getFileExtension(Uri uri) {
        return getContentResolver().getType(uri);
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

    // Method to get image URI from bitmap
    // Method to get image URI from bitmap
    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}
