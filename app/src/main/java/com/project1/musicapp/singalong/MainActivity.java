package com.project1.musicapp.singalong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.project1.musicapp.singalong.MESSAGE";
    Button login, createAcc, forgotPass, fbButton;
    EditText mail, pass;
    TextView txt;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth auth;
    private TextView t11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("181301999128140");
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        FacebookSdk.getApplicationContext();


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Language.class));
            finish();
        }


        login = (Button) findViewById(R.id.loginButton);
        createAcc = (Button) findViewById(R.id.createAccountButton);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "Cannot connect", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

            }
        });


        mail = (EditText) findViewById(R.id.mailText);
        pass = (EditText) findViewById(R.id.passwordText);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        forgotPass = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPass.setVisibility(View.VISIBLE);
        t11 = (TextView) findViewById(R.id.textView9);
        forgotPass.setBackgroundColor(Color.TRANSPARENT);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mail.getText().toString();
                final String password = pass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                t11.setText("Connecting....");


                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    {
                                        t11.setText("Login Failed!");
                                    }
                                } else {
                                    Intent intent = new Intent(MainActivity.this, Language.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call(view);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(MainActivity.this, Language.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                });
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(MainActivity.this, Language.class);
        startActivity(intent);
    }

    public void sendMessage2(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
        startActivity(intent);
    }


    /*public void call(View view){
        Intent intent= new Intent(MainActivity.this, RegistrationPage.class);
        startActivity(intent);
    }*/
}
