package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPage extends AppCompatActivity {
    Button register, imgChange;
    EditText name, email, password, username;
    TextView t1, header;
    Typeface tf3;
    public int currentImage = 0;
    public int numOfImage = 9;
    ImageView logo;
    private FirebaseAuth auth;
    private TextWatcher text = null;
    private String Name, n;
    private int p;

    int[] images = {R.drawable.bitstrip_cool, R.drawable.bitstrip_headphone, R.drawable.bitstrip_hijab, R.drawable.bitstrip_mj, R.drawable.bitstrip_phonaholic, R.drawable.bitstrip_sad, R.drawable.bitstrip_salute, R.drawable.bitstrip_yay, R.drawable.bitstrip_lolly};
    private ProgressBar progressBar;
    private ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regpage);

        auth = FirebaseAuth.getInstance();


        register = (Button) findViewById(R.id.Regge);
        //imgChange=(Button) findViewById(R.id.imageChange);

        //imgChange.setVisibility(View.VISIBLE);
        //imgChange.setBackgroundColor(Color.TRANSPARENT);
        //t1=(TextView)  findViewById(R.id.selectemoji);
        header = (TextView) findViewById(R.id.header);


        tf3 = Typeface.createFromAsset(getAssets(), "Montserrat-ExtraBold.ttf");
        header.setTypeface(tf3);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        prog = (ProgressBar) findViewById(R.id.prog);
        prog.setVisibility(View.INVISIBLE);


        text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                username.setText(name.getText().toString());
                Name = name.getText().toString();
                if (Name.contains(" ")) {
                    p = Name.indexOf(" ");
                    n = Name.substring(0, p);
                    username.setText(n);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        name.addTextChangedListener(text);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                final String UserName = username.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                prog.setVisibility(View.VISIBLE);


                auth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(RegistrationPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                prog.setVisibility(View.GONE);
                                Toast.makeText(RegistrationPage.this, "Registration Complete!", Toast.LENGTH_SHORT).show();


                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrationPage.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("propic").setValue("g2");
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username").setValue(UserName);

                                    startActivity(new Intent(RegistrationPage.this, Language.class));
                                    finish();
                                }
                            }

                        });
            }
        });



        /*imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentImage++;
                currentImage=currentImage%images.length;
                logo.setImageResource(images[currentImage]);



            }
        });*/


    }

    public void gogo(View view) {
        Intent intent = new Intent(RegistrationPage.this, Language.class);
        startActivity(intent);
    }

}