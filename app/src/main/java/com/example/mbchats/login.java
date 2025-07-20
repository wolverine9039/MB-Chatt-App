package com.example.mbchats;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button login_button;
    EditText Email, edpass;
    TextView Signup;

    String email_pattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();
        login_button=findViewById(R.id.login_button);
        Email=findViewById(R.id.Log_Email);
        edpass=findViewById(R.id.log_Password);
        Signup=findViewById(R.id.textView8Signup);

        Signup.setOnClickListener(v -> {
            Intent ikl=new Intent(login.this,Registration.class);
            startActivity(ikl);
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString();
                String pass=edpass.getText().toString();

                if((TextUtils.isEmpty(email)))
                {
                  Toast.makeText(login.this, "Enter email",Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(pass))) {
                    Toast.makeText(login.this, "Enter the password",Toast.LENGTH_SHORT).show();
                } else if (!email.matches(email_pattern)) {
                    Email.setError("Give proper Email Address");
                } else if (pass.length()<6) {
                    edpass.setError("not valid");
                    Toast.makeText(login.this,"must be more than 6 digits",Toast.LENGTH_SHORT).show();

                }
                else{
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                try {
                                    Intent i=new Intent(login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(login.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });







    }
}