package com.project1.musicapp.singalong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Image extends AppCompatActivity {

    ImageView g2, man3, man1, boy, girl1, girl6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageset);

        g2=findViewById(R.id.g2);
        man3=findViewById(R.id.man3);
        man1=findViewById(R.id.man1);
        boy=findViewById(R.id.boy);
        girl1=findViewById(R.id.girl1);
        girl6=findViewById(R.id.girl6);

        View.OnClickListener setProPic = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int ID=view.getId();
                String imageName=getResources().getResourceEntryName(ID);

                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("propic").setValue(imageName);
                finish();
            }
        };

        g2.setOnClickListener(setProPic);
        man3.setOnClickListener(setProPic);
        man1.setOnClickListener(setProPic);
        boy.setOnClickListener(setProPic);
        girl1.setOnClickListener(setProPic);
        girl6.setOnClickListener(setProPic);

    }
}
