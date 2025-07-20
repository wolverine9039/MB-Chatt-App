package com.example.mbchats;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    TextView loginb;
    EditText RGuname, RGemail, RGPass, RGREpass;
    Button Signup;
    FirebaseDatabase database;
    FirebaseStorage storagee;
    CircleImageView RGprofileimage;
    FirebaseAuth auth;
    Uri imageURI;
    String Imageuri;
    String statuss = "Hey I am Using This application";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storagee = FirebaseStorage.getInstance();

        loginb = findViewById(R.id.login);
        RGuname = findViewById(R.id.editTextRGusername);
        RGemail = findViewById(R.id.editTextRGEmailAddress);
        RGPass = findViewById(R.id.editTextRGPassword);
        RGREpass = findViewById(R.id.editRG_RePassword);
        Signup = findViewById(R.id.Signup1);
        RGprofileimage = findViewById(R.id.profile_image);

        loginb.setOnClickListener(v -> {
            startActivity(new Intent(Registration.this, login.class));
            finish();
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndRegisterUser();
            }
        });

        RGprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfileImage();
            }
        });
    }

    private void validateAndRegisterUser() {
        String namee = RGuname.getText().toString().trim();
        String Emaill = RGemail.getText().toString().trim();
        String passss = RGPass.getText().toString();
        String REpass = RGREpass.getText().toString();

        if (TextUtils.isEmpty(namee)) {
            RGuname.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(Emaill)) {
            RGemail.setError("Please enter your email");
            return;
        }

        if (!Emaill.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")) {
            RGemail.setError("Invalid email format");
            return;
        }

        if (TextUtils.isEmpty(passss)) {
            RGPass.setError("Please enter password");
            return;
        }

        if (passss.length() < 6) {
            RGPass.setError("Password must be at least 6 characters");
            return;
        }

        if (TextUtils.isEmpty(REpass)) {
            RGREpass.setError("Please confirm password");
            return;
        }

        if (!passss.equals(REpass)) {
            RGREpass.setError("Passwords don't match");
            return;
        }

        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(Emaill, passss)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String UniqID = firebaseUser.getUid();
                                saveUserData(UniqID, namee, Emaill, passss, REpass);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "Registration failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Registration failed", task.getException());
                        }
                    }
                });
    }

    private void saveUserData(String UniqID, String namee, String Emaill, String passss, String REpass) {
        progressDialog.setMessage("Saving user data...");

        DatabaseReference refrencde = database.getReference("users").child(UniqID);
        StorageReference STR = storagee.getReference("profile_images").child(UniqID);

        if (imageURI != null) {
            STR.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            STR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Imageuri = uri.toString();
                                    Users users = new Users(UniqID, namee, Emaill, passss, REpass, Imageuri, statuss);
                                    saveUserToDatabase(refrencde, users);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Image upload failed, using default image", e);
                            Imageuri = "https://firebasestorage.googleapis.com/v0/b/mb-chats.appspot.com/o/default_profile.png?alt=media";
                            Users users = new Users(UniqID, namee, Emaill, passss, REpass, Imageuri, statuss);
                            saveUserToDatabase(refrencde, users);
                        }
                    });
        } else {
            Imageuri = "https://firebasestorage.googleapis.com/v0/b/mb-chats.appspot.com/o/default_profile.png?alt=media";
            Users users = new Users(UniqID, namee, Emaill, passss, REpass, Imageuri, statuss);
            saveUserToDatabase(refrencde, users);
        }
    }

    private void saveUserToDatabase(DatabaseReference refrencde, Users users) {
        refrencde.setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(Registration.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Database write failed", task.getException());
                        }
                    }
                });
    }

    private void selectProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            RGprofileimage.setImageURI(imageURI);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}