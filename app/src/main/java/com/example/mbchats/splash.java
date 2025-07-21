package com.example.mbchats;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView appname,o1,o2;
    Animation topanimation,Bottomanimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logo=findViewById(R.id.logoimg);
        appname=findViewById(R.id.logonameimg);
        o1=findViewById(R.id.own1);
        o2=findViewById(R.id.own2);

        topanimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        Bottomanimation= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(topanimation);
        appname.setAnimation(Bottomanimation);
        o1.setAnimation(Bottomanimation);
        o2.setAnimation(Bottomanimation);

        new Handler().postDelayed(() -> {
            Intent intent=new Intent(splash.this, login.class);

            startActivity(intent);
            finish();
        }, 3000);
    }
}