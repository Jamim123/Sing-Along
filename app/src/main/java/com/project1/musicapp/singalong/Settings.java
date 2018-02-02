package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jamim on 11/30/2017.
 */

public class Settings extends AppCompatActivity  {

    Button logOut;
    public Spinner dropdown;
    TextView tt1,tt2,tt3;
    private TextView t2;
    private Button Logout;
    private FirebaseAuth auth;
    Typeface tf3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        tt1=(TextView) findViewById(R.id.lang);
        tt2=(TextView) findViewById(R.id.userinfo);

        tt3=(TextView) findViewById(R.id.rateapp);
        logOut=findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Settings.this, MainActivity.class));
                    finishAffinity();
                }

    });


        tt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this,Profilepage.class);
                startActivity(intent);



            }
        });






        //tf3= Typeface.createFromAsset(getAssets(),"Montserrat-ExtraBold.ttf");
       // tt1.setTypeface(tf3);
        //tt2.setTypeface(tf3);
        //tt3.setTypeface(tf3);




    }
}
