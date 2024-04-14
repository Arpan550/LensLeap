package com.example.lensleap.ui.newposts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lensleap.R;
import com.example.lensleap.databinding.ActivityNewReelsBinding;

public class NewReelsActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;

    private ActivityNewReelsBinding newReelsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newReelsBinding = ActivityNewReelsBinding.inflate(getLayoutInflater());
        setContentView(newReelsBinding.getRoot());

        newReelsBinding.buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadOptionsDialog();
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
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();
                newReelsBinding.videoView.setVideoURI(videoUri);
                newReelsBinding.videoView.start();
            } else if (requestCode == REQUEST_GALLERY) {
                Uri selectedVideo = data.getData();
                newReelsBinding.videoView.setVideoURI(selectedVideo);
                newReelsBinding.videoView.start();
            }
            controlVideo(newReelsBinding.videoView);
        }
    }

    private void controlVideo(VideoView videoView) {
        MediaController mc=new MediaController(this);
        videoView.setMediaController(mc);
        mc.setAnchorView(videoView);
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
