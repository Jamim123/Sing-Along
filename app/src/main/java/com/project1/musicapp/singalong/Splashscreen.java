package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class Splashscreen extends AppCompatActivity {
    //private int counter1, finalVal;
    //private String sloganText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

       Typeface tf= Typeface.createFromAsset(getAssets(),"OpenSans-Bold.ttf");




        TextView slogan = (TextView) findViewById(R.id.appslogan);
       // TextView name=findViewById(R.id.name);
        //name.setTypeface(tf);

        slogan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Splashscreen.this, AddSongs.class));
            }
        });

        //sloganText = "Weather - SING IT!";

        //TextViewAnimation(0, 250, slogan);


        //slogan.setText(sloganText);

        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException en) {
                    en.printStackTrace();
                } finally {
                    Intent i = new Intent(Splashscreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();




        //Toast.makeText(getApplicationContext(), slogan.getText(), Toast.LENGTH_LONG).show();
    }


    //public void TextViewAnimation(int start, int finish, final TextView slogan) {
       // counter1 = start;
        //finalVal = finish;

        //new Thread(new Runnable() {
           // public void run() {
                //while(counter1<finalVal) {
                   // try {
                        //sleep(10);
                   // } catch (InterruptedException e) {
                       // e.printStackTrace();
                    //}

                    //slogan.post(new Runnable() {
                       // @Override
                        //public void run() {
                           // Random rand = new Random();
                            //int[] indices = new int[]{0, 1, 2, 3, 4, 5, 6, 10, 11, 12, 13, 15, 16, 17};
                           // String ret = "Weather - SING IT!";
                            //for(int position: indices) {
                                //char ch = ret.charAt(position);
                                //if(Character.isUpperCase(ch)) {
                                    //ret = ret.substring(0, position) + String.valueOf( (char) ((int)'A' + (rand.nextInt() % 26)) ) + ret.substring(position + 1);
                               // } else {
                                   // ret = ret.substring(0, position) + String.valueOf( (char) ((int)'a' + ((rand.nextInt()) % 26)) ) + ret.substring(position + 1);
                                //}
                           // }
                           // if(counter1*2>=finalVal) ret = "Weather - SING IT!";
                           // slogan.setText(ret);
                      //  }
                   // });
                    //counter1++;

                //}
                //slogan.setText(sloganText);(not needed)
            //}
       // }).start();


    }

//    String generateSlogan() {(not needed)
//        Random rand = new Random();
//        int[] indices = new int[]{0, 1, 2, 3, 4, 5, 6, 10, 11, 12, 13, 15, 16, 17};
//        String ret = "Weather - SING IT!";
//        for(int position: indices) {
//            char ch = ret.charAt(position);
//            if(Character.isUpperCase(ch)) {
//                ret = ret.substring(0, position) + ('A' + (rand.nextInt() % 26)) + ret.substring(position + 1);
//            } else {
//                ret = ret.substring(0, position) + ('a' + (rand.nextInt() % 26)) + ret.substring(position + 1);
//            }
//        }
//        Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG);
//        return ret;
//    }

