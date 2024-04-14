package com.example.lensleap;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lensleap.LogInActivity;
import com.example.lensleap.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding signUpBinding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;
    ProgressDialog progressDialog;
    Intent iNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        // Initialize FirebaseAuth instance and progress dialog
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);


        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class ));
            finish();
        }

        signUpBinding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = signUpBinding.editTextUsername.getText().toString();
                String email = signUpBinding.editTextEmail.getText().toString().trim();
                String password = signUpBinding.editTextPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    signUpBinding.editTextUsername.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    signUpBinding.editTextEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    signUpBinding.editTextPassword.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    signUpBinding.editTextPassword.setError("Password must be greater than or equal to 6 character");
                }

                // Show progress dialog
                progressDialog.setMessage("Creating account...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // create account
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                            //store user data in firestore database
                            userID = auth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("users").document(userID);
                            Map<String, Object> user=new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            user.put("password", password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user profile is created for "+userID);
                                }
                            });

                            // Going to next activity
                            iNext=new Intent(getApplicationContext(), AccountSetUpActivity.class );
                            iNext.putExtra("username", username);
                            startActivity(iNext);
                            finish();
                        } else{
                            Toast.makeText(SignUpActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signUpBinding.textViewLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        signUpBinding.editTextUsername.setText("");
        signUpBinding.editTextEmail.setText("");
        signUpBinding.editTextPassword.setText("");
    }

}
