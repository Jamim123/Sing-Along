package com.project1.musicapp.singalong;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.project1.musicapp.singalong.R.layout.profilepage;

/**
 * Created by jamim on 11/6/2017.
 */

public class Profilepage extends AppCompatActivity {

    Button changeImage;
    Button saveChanges;
    Button logout;

    ImageView propic;

    EditText userName;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(profilepage);

        changeImage = findViewById(R.id.btnChangeImage);
        saveChanges = findViewById(R.id.saveChanges);
        logout = findViewById(R.id.logOut);

        propic = findViewById(R.id.propic);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usernameText = dataSnapshot.child("username").getValue(String.class);
                userName.setText(usernameText);

                String bgImage = dataSnapshot.child("propic").getValue(String.class);
                Resources res = getResources();
                int resID = res.getIdentifier(bgImage , "drawable", getPackageName());
                propic.setImageResource(resID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       saveChanges.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String usernameText = userName.getText().toString();
               String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
               FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username").setValue(usernameText);
           }
       });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profilepage.this, MainActivity.class));
                finishAffinity();
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profilepage.this, Image.class));
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
