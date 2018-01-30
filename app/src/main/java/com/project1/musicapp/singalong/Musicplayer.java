package com.project1.musicapp.singalong;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;


public class Musicplayer extends AppCompatActivity implements Runnable, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String APPID = "013a7b3018e42daf9e080541c8e9fcb6";
    private GoogleApiClient googleApiClient;
    public TextView t1;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;


    //from singalong
    ProgressDialog dialog;
    private boolean started = false;
    private long currentPosition = 0;
    public MediaPlayer mp;
    public SeekBar songSeekBar;
    String applicationID = "260462";
    protected DeezerConnect deezerConnect = null;
    public Thread soundThread;
    public String weather = "rainy";
    public TreeMap<String, SongAction> data;
    ImageButton playButton, nextButton, previousButton,heartButton,options;
    TextView songName, artistName, album, txtView, showtext, t11;
    ImageView backgroundAlbumImage;
    Button  m1;
    ImageView heart;
    ProgressBar prog;
    View imm;
    FirebaseAuth mAuth;
    String message;
    DatabaseReference dref;

    SongAction currentSong;
    TrackPlayer player;
    private String lang;

    //from singalong


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_musicplayer);
        setContentView(R.layout.l1);


        txtView = (TextView) findViewById(R.id.rd);
        txtView.setText(getIntent().getStringExtra("nm"));
        txtView.setVisibility(View.INVISIBLE);
        lang = txtView.getText().toString();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading weather and song data...");
        dialog.show();


        mAuth = FirebaseAuth.getInstance();
        deezerConnect = new DeezerConnect(this, applicationID);
        dref = FirebaseDatabase.getInstance().getReference();


        t1 = (TextView) findViewById(R.id.weather);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }


        heartButton = (ImageButton) findViewById(R.id.btnLove);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TransitionDrawable) heartButton.getDrawable()).startTransition(2000);
            }
        });




        googleApiClient = new GoogleApiClient.Builder(this, this, (GoogleApiClient.OnConnectionFailedListener) this).addApi(LocationServices.API).build();


        nextButton = (ImageButton) findViewById(R.id.forw);

        //TextView t3 = (TextView) findViewById(R.id.textView3);
        playButton = (ImageButton) findViewById(R.id.play);


        previousButton = (ImageButton) findViewById(R.id.prev);



        songSeekBar = (SeekBar) findViewById(R.id.seekBar);

        options=(ImageButton) findViewById(R.id.btnPlaylist);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Musicplayer.this,Settings.class);
                startActivity(i);
            }
        });

        songName = (TextView) findViewById(R.id.song);
        artistName = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        backgroundAlbumImage = (ImageView) findViewById(R.id.backgroundImage);


        Intent returnIntent = getIntent();
        setResult(RESULT_OK, returnIntent);


        /*BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.usersss:
                                Intent i = new Intent(Musicplayer.this, Profilepage.class);
                                startActivity(i);

                            case R.id.languagee:
                                Intent ii = new Intent(Musicplayer.this, Language.class);
                                startActivity(ii);

                            case R.id.logoutt:
                                mAuth.signOut();
                                LoginManager.getInstance().logOut();
                                if (mAuth.getCurrentUser() == null) {
                                    startActivity(new Intent(Musicplayer.this, MainActivity.class));
                                    finish();
                                }


                        }
                        return true;
                    }
                });*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Nothing to do
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (player == null) {
            try {
                player = new TrackPlayer(getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
            } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
                tooManyPlayersExceptions.printStackTrace();
            } catch (DeezerError deezerError) {
                deezerError.printStackTrace();
            }
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            String units = "imperial";
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                    lat, lon, units, APPID);


            //mp = new MediaPlayer();
            new GetWeatherData(this, t1, weather).execute(url);

            double x = 22;


        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    public void setSong() {
        currentSong = data.get(weather);

//        if (mp != null) {
//            mp.stop();
//        }
//        mp = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(currentSong.getUrl(), "raw", getPackageName()));
//        background.setImageResource(getResources().getIdentifier(currentSong.getback(), "drawable", getPackageName()));
        started = false;
        currentPosition = 0;

        long trackid = 0;

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
        dref.child("Weather").child(weather).child("Songs").child(lang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SongSkeleton song = child.child("songData").getValue(SongSkeleton.class);
                    currentSong.addSong(song);
                }

                dialog.cancel();
                Toast.makeText(Musicplayer.this, String.valueOf(currentSong.getSize()), Toast.LENGTH_LONG).show();

                songName.setText(currentSong.getSongName());
                songName.setSelected(true);
                songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                songName.setSingleLine(true);
                artistName.setText(currentSong.getArtist());
                album.setText(currentSong.getAlbum());
               // backgroundAlbumImage.setBackground(currentSong.getback());

                player.playTrack(currentSong.getUrl());
                player.pause();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void setupListeners() {
        //Toast.makeText(this, "alfa", Toast.LENGTH_LONG).show();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (started) {
                    if (player != null) {
                        //playButton.setBackgroundResource(R.drawable.img_btn_pause_pressed);
                        player.pause();
                        playButton.setImageResource(R.drawable.img_btn_play_pressed);
                        currentPosition = player.getTrackDuration();
                        started = false;
                    }
                } else {
                    if (player == null) {
                        setSong();
                    }
                    player.seek(currentPosition);
                    player.play();
                    playButton.setImageResource(R.drawable.img_btn_pause_pressed);
                    started = true;
                }
                //Toast.makeText(Musicplayer.this, weather, Toast.LENGTH_LONG).show();
                //Toast.makeText(Musicplayer.this, currentSong.getUrl().toString(), Toast.LENGTH_LONG).show();

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) player.stop();
                currentSong = data.get(weather);
                currentSong.Next();
                boolean prevstarted = started;
                setSong();

                player.playTrack(currentSong.getUrl());

                if (prevstarted) {
                    started = true;
                } else {
                    player.pause();
                    started = false;
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                if (player != null) player.stop();
            currentSong = data.get(weather);
            currentSong.Previous();
                //data.get(weather).Previous();
                boolean prevstarted = started;
                setSong();
            player.playTrack(currentSong.getUrl());
                if (prevstarted) {
                    player.pause();
                    started = true;
                }
            }
        });

//        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    mp.seekTo(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    @Override
    public void run() {
        int soundTotal = mp.getDuration();
        songSeekBar.setMax(soundTotal);

        while (mp != null && currentPosition < soundTotal) {
            soundTotal = mp.getDuration();
            songSeekBar.setMax(soundTotal);
            try {
                Thread.sleep(300);
                //currentPosition = mp.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            //songSeekBar.setProgress(currentPosition);
        }
    }

}
