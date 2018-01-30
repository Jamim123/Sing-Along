package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.project1.musicapp.singalong.R.layout.profilepage;

/**
 * Created by jamim on 11/6/2017.
 */

public class Profilepage extends AppCompatActivity {

    Button b1;
    Button avv;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(profilepage);
        b1=(Button)findViewById(R.id.av);
        b1.setVisibility(View.VISIBLE);
        b1.setBackgroundColor(Color.TRANSPARENT);
        avv=(Button)findViewById(R.id.av);

        avv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt=new Intent(Profilepage.this,Image.class);
                startActivity(intt);
            }
        });


        /*Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            String
        }*/



        /*b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profilepage.this,Image.class);
                startActivity(intent);
            }
        }); */
    }
}
