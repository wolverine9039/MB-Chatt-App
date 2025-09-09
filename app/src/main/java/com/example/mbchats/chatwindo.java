package com.example.mbchats;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwindo extends AppCompatActivity {
    String recieverimg,recieverUid,recieverName,SenderUid;
    CircleImageView profile;
    TextView recieverNName;
    CardView sendbtn;
    EditText textmsg;
     FirebaseAuth firebaseAuth;
     FirebaseDatabase database;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatwindo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recieverName= getIntent().getStringExtra("nameee");
        recieverimg=getIntent().getStringExtra("recieverImg");
        recieverUid=getIntent().getStringExtra("uid");

        sendbtn=findViewById(R.id.sendbtnn);
        textmsg=findViewById(R.id.textmsg);

        profile=findViewById(R.id.profileimgg);
        recieverNName=findViewById(R.id.recivername);

        Picasso.get().load(recieverimg).into(profile);
        recieverNName.setText(""+recieverName);

        DatabaseReference reference= database.getReference().child("user").child(firebaseAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SenderUid=firebaseAuth.getUid();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messsage=textmsg.getText().toString();
                if(messsage.isEmpty())
                {
                    Toast.makeText(chatwindo.this,"Enter msg first",Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date date=new Date();
                msgModelclass messages= new msgModelclass(messsage,SenderUid,date.getTime());

            }
        });

    }
}