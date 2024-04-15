package com.example.lensleap.ui.newposts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lensleap.MainActivity;
import com.example.lensleap.databinding.ActivityNewReelsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class NewReelsActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;

    private ActivityNewReelsBinding newReelsBinding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri videoUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newReelsBinding = ActivityNewReelsBinding.inflate(getLayoutInflater());
        setContentView(newReelsBinding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("videos");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading video...");
        progressDialog.setCancelable(false);

        newReelsBinding.buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadOptionsDialog();
            }
        });

        newReelsBinding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideoToFirestore();
            }
        });
    }

    private void showUploadOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an uploading option");
        builder.setItems(new CharSequence[]{"Record Video", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        requestCameraPermission();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            dispatchTakeVideoIntent();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE || requestCode == REQUEST_GALLERY) {
                videoUri = data.getData();
                newReelsBinding.videoView.setVideoURI(videoUri);
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(newReelsBinding.videoView);
                newReelsBinding.videoView.setMediaController(mediaController);
                newReelsBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // Start playing the video
                        mediaPlayer.start();
                    }
                });
            }
        }
    }



    private void uploadVideoToFirestore() {
        if (videoUri != null) {
            progressDialog.show();
            StorageReference videoRef = storageReference.child(System.currentTimeMillis() + ".mp4");
            videoRef.putFile(videoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        // Video uploaded successfully, get the download URL
                        videoRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Save video URL to Firestore
                            saveVideoUrlToFirestore(downloadUri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewReelsActivity.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVideoUrlToFirestore(String videoUrl) {
        String caption = newReelsBinding.editTextCaption.getText().toString();
        String userID = auth.getCurrentUser().getUid();

        // Reference the collection where you want to add the new document
        CollectionReference collectionReference = firestore.collection("userReels");

        Map<String, Object> userReels = new HashMap<>();
        userReels.put("caption", caption);
        userReels.put("videoUrl", videoUrl);
        userReels.put("userID", userID);

        // Use add() to add a new document to the collection
        collectionReference.add(userReels).addOnSuccessListener(documentReference -> {
            // Hide progress dialog
            progressDialog.dismiss();
            startActivity(new Intent(NewReelsActivity.this, MainActivity.class));
            finish();
            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            //Log.w(TAG, "Error adding document", e);
            // Handle failure
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
