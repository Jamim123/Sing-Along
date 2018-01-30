package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by jamim on 12/9/2017.
 */

public class Language extends AppCompatActivity {
    Typeface tf3;
    TextView t1;
    private RadioGroup radioGroup;
    private String eng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_and_image_page);

        t1=(TextView) findViewById(R.id.languagePageHeader);


        tf3= Typeface.createFromAsset(getAssets(),"Montserrat-ExtraBold.ttf");
        t1.setTypeface(tf3);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (rb!=null && checkedId > -1)
                {
                   if(checkedId==R.id.radioButton)
                   {
                       eng="English";
                       Intent ind=new Intent(Language.this,Musicplayer.class);
                       ind.putExtra("nm",eng);
                       startActivity(ind);
                       finish();
                   }
                    if(checkedId==R.id.radioButton2)
                    {
                        eng="Bangla";
                        Intent ind=new Intent(Language.this,Musicplayer.class);
                        ind.putExtra("nm",eng);
                        startActivity(ind);
                        finish();
                    }
                    if(checkedId==R.id.radioButton3)
                    {
                        eng="Does't matter";
                        Intent ind=new Intent(Language.this,Musicplayer.class);
                        ind.putExtra("nm",eng);
                        startActivity(ind);
                        finish();
                    }

                   }
                }



    });
        radioGroup.clearCheck();

}

public void googo()
{
    Intent ind=new Intent(Language.this,Musicplayer.class);
    startActivity(ind);
    finish();
}


    public void onClear(View v) {
        radioGroup.clearCheck();
    }
}
