package com.project1.musicapp.singalong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

public class Image extends AppCompatActivity {

    ImageView i1;
    Button b11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //b11=(Button)findViewById(R.id.b1);
        //b11.setVisibility(View.VISIBLE);
       // b11.setBackgroundColor(Color.TRANSPARENT);

       /* b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Image.this,Profilepage.class);
                intent.putExtra("Picture",R.drawable.bitstrip_headphone);
                startActivity(intent);
            }
        });*/


    }
}
