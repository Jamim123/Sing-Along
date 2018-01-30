package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jamim on 11/30/2017.
 */

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button b1;
    public Spinner dropdown;
    TextView t1;
    private TextView t2;
    private Button Logout;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        dropdown = (Spinner) findViewById(R.id.spinner2);
        t1 = (TextView) findViewById(R.id.textView4);
        Logout=(Button)findViewById(R.id.Logout);
        auth = FirebaseAuth.getInstance();



        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.Choices, android.R.layout.simple_list_item_1);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        b1 = (Button) findViewById(R.id.userInfoButton);
        b1.setVisibility(View.VISIBLE);
        b1.setBackgroundColor(Color.TRANSPARENT);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(Settings.this, Profilepage.class);
                startActivity(intt);
            }
        });

        dropdown.setOnItemSelectedListener(this);

        Logout.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          signOut();

                                      }

    });
    }

    //sign out method
    public void signOut() {
        auth.signOut();


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:

                break;
            case 1:
                t2=(TextView)v;
                t1.setText(t2.getText());
                t1.setTextColor(Color.WHITE);
                t1.setTextSize(23);
                break;
            case 2:
                t2=(TextView)v;
                t1.setText(t2.getText());
                t1.setTextColor(Color.WHITE);
                t1.setTextSize(23);
                break;
            case 3:
                t2=(TextView)v;
                t1.setText(t2.getText());
                t1.setTextColor(Color.WHITE);
                t1.setTextSize(15);
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
