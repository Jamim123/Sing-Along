package com.project1.musicapp.singalong;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.event.OnPlayerProgressListener;
import com.deezer.sdk.player.event.OnPlayerStateChangeListener;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;


public class Musicplayer extends AppCompatActivity implements Runnable, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String APPID = "013a7b3018e42daf9e080541c8e9fcb6";
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;
    public TextView t1;


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
    ImageButton playButton, nextButton, previousButton, heartButton, options;
    TextView songName, artistName, album, txtView, showtext, t11;
    ImageView backgroundAlbumImage;
    Button m1;
    ImageView heart;
    TextView nextSong;
    ProgressBar prog;
    View imm;
    FirebaseAuth mAuth;
    String message;
    DatabaseReference dref;

    SongAction currentSong;
    TrackPlayer player;
    String lang;

    OnPlayerStateChangeListener normalListener;
    OnPlayerStateChangeListener initialListener;
    View.OnClickListener blank;
    View.OnClickListener play;
    View.OnClickListener pause;
    Thread timeShower;

    TextView timeElapsed, timeRemaining;

    GoogleApiClient locationApiClient;
    OnPlayerProgressListener progressListener;
    //from singalong


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l1);


        txtView = (TextView) findViewById(R.id.rd);
        txtView.setText(getIntent().getStringExtra("nm"));
        txtView.setVisibility(View.INVISIBLE);
        lang = txtView.getText().toString();

        timeElapsed = findViewById(R.id.songCurrentDurationLabel);
        timeRemaining = findViewById(R.id.songTotalDurationLabel);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading weather and song data...");
        dialog.show();


        mAuth = FirebaseAuth.getInstance();
        deezerConnect = new DeezerConnect(this, applicationID);
        dref = FirebaseDatabase.getInstance().getReference();


        t1 = (TextView) findViewById(R.id.weather);
        heartButton = (ImageButton) findViewById(R.id.btnLove);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TransitionDrawable) heartButton.getDrawable()).startTransition(2000);


            }
        });


        googleApiClient = new GoogleApiClient.Builder(this, this, (GoogleApiClient.OnConnectionFailedListener) this).addApi(LocationServices.API).build();


        nextButton = (ImageButton) findViewById(R.id.forw);
        playButton = (ImageButton) findViewById(R.id.play);
        previousButton = (ImageButton) findViewById(R.id.prev);
        songSeekBar = (SeekBar) findViewById(R.id.seekBar);
        options = (ImageButton) findViewById(R.id.btnPlaylist);
        songName = (TextView) findViewById(R.id.song);
        artistName = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        backgroundAlbumImage = (ImageView) findViewById(R.id.backgroundImage);
        nextSong = (TextView) findViewById(R.id.nextSongTitle);


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Musicplayer.this, Settings.class);
                startActivity(i);
            }
        });


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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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

    public void setSong(final boolean startAutomatically) {
        currentSong = data.get(weather);

        started = false;
        currentPosition = 0;


        songName.setText(currentSong.getSongName());
        songName.setSelected(true);
        songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songName.setSingleLine(true);

        artistName.setText(currentSong.getArtist());
        album.setText(currentSong.getAlbum());

        nextSong.setText(currentSong.getNextSongName());
        //nextSong.setSelected(true);
        nextSong.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        nextSong.setSingleLine(true);

        Glide.with(Musicplayer.this).load(currentSong.getback()).into(backgroundAlbumImage);

        //int dataa= Integer.parseInt(currentSong.getback());
        //backgroundAlbumImage.setImageResource(dataa);

        player.playTrack(currentSong.getUrl());

        progressListener = new OnPlayerProgressListener() {

            @Override
            public void onPlayerProgress(long l) {
                String elapsedMinutes = String.valueOf(l/60000);
                if(elapsedMinutes.length()==1) elapsedMinutes = "0"+elapsedMinutes;

                String elapsedSeconds = String.valueOf((l/1000)%60);
                if(elapsedSeconds.length()==1) elapsedSeconds = "0"+elapsedSeconds;

                timeElapsed.setText(elapsedMinutes + ":" + elapsedSeconds);

                long timeLeft = player.getTrackDuration() - l;

                String leftMinutes = String.valueOf(timeLeft/60000);
                if(leftMinutes.length()==1) leftMinutes = "0"+leftMinutes;

                String leftSeconds = String.valueOf((timeLeft/1000)%60);
                if(leftSeconds.length()==1) leftSeconds = "0"+leftSeconds;

                timeRemaining.setText(leftMinutes + ":" + leftSeconds);
            }
        };

        blank = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        play = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player == null) setSong(true);
                player.seek(currentPosition);
                player.play();
            }
        };

        pause = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.pause();
                currentPosition = player.getTrackDuration();
            }
        };

        soundThread = new Thread(Musicplayer.this);

        normalListener = new OnPlayerStateChangeListener() {
            @Override
            public void onPlayerStateChange(PlayerState playerState, long l) {
                if (playerState.equals(PlayerState.WAITING_FOR_DATA)) {
                    playButton.setOnClickListener(play);
                    playButton.setImageResource(R.drawable.img_btn_play);
                } else if (playerState.equals(PlayerState.STOPPED)) {
                    playButton.setOnClickListener(play);
                    playButton.setImageResource(R.drawable.img_btn_play);
                } else if (playerState.equals(PlayerState.INITIALIZING)) {
                    playButton.setOnClickListener(play);
                    playButton.setImageResource(R.drawable.img_btn_play);
                } else if (playerState.equals(PlayerState.PAUSED)) {
                    playButton.setOnClickListener(play);
                    playButton.setImageResource(R.drawable.img_btn_play);
                } else if (playerState.equals(PlayerState.READY)) {
                    playButton.setOnClickListener(play);
                    playButton.setImageResource(R.drawable.img_btn_play);
                } else if (playerState.equals(PlayerState.PLAYBACK_COMPLETED)) {
                    Toast.makeText(Musicplayer.this, "Playback Stopped", Toast.LENGTH_LONG).show();

                    playButton.setImageResource(R.drawable.img_btn_play);

                    try {
                        player.removeOnPlayerProgressListener(progressListener);
                        player.removeOnPlayerStateChangeListener(this);
                        player = new TrackPlayer(getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
                    } catch (TooManyPlayersExceptions | DeezerError tooManyPlayersExceptions) {
                        tooManyPlayersExceptions.printStackTrace();
                    }

                    currentSong = data.get(weather);
                    currentSong.Next();
                    player.removeOnPlayerStateChangeListener(normalListener);
                    setSong(true);

                } else if (playerState.equals(PlayerState.PLAYING)) {
                    if (!soundThread.isAlive()) soundThread.start();
                    player.addOnPlayerProgressListener(progressListener);
                    Toast.makeText(Musicplayer.this, "Playing now...", Toast.LENGTH_LONG).show();
                    playButton.setOnClickListener(pause);
                    playButton.setImageResource(R.drawable.img_btn_pause_pressed);

                }
            }
        };

        initialListener = new OnPlayerStateChangeListener() {
            @Override
            public void onPlayerStateChange(PlayerState playerState, long l) {
                if (playerState.equals(PlayerState.INITIALIZING)) {
                    playButton.setOnClickListener(blank);
                } else if (playerState.equals(PlayerState.PLAYING)) {
                    if (!startAutomatically) player.pause();
                    else player.play();
                    //Toast.makeText(Musicplayer.this, "Playing", Toast.LENGTH_LONG).show();
                    player.addOnPlayerStateChangeListener(normalListener);
                    player.removeOnPlayerStateChangeListener(initialListener);
                }
            }
        };

        player.addOnPlayerStateChangeListener(normalListener);
    }


    public void setupListeners() {
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (started) {
//                    if (player != null) {
//                        //playButton.setBackgroundResource(R.drawable.img_btn_pause_pressed);
//                        player.pause();
//                        playButton.setImageResource(R.drawable.img_btn_play);
//                        currentPosition = player.getTrackDuration();
//                        started = false;
//                    }
//                } else {
//                        if (player == null) {
//                        setSong(false);
//                        Toast.makeText(Musicplayer.this, weather, Toast.LENGTH_LONG).show();
//                        playButton.setImageResource(R.drawable.img_btn_pause_pressed);
//                        //player.playTrack(currentSong.getUrl());
//                    }
//                    player.seek(currentPosition);
//                    player.play();
//                    playButton.setImageResource(R.drawable.img_btn_pause_pressed);
//                    started = true;
//                }
//            }
//        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) player.stop();
                currentSong = data.get(weather);
                currentSong.Next();
                boolean prevstarted = started;
                player.removeOnPlayerStateChangeListener(normalListener);
                setSong(prevstarted);

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
                player.removeOnPlayerStateChangeListener(normalListener);
                setSong(prevstarted);
                player.playTrack(currentSong.getUrl());
                if (prevstarted) {
                    player.pause();
                    started = true;
                }
            }
        });

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seek(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void run() {
        long soundTotal = player.getTrackDuration();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Musicplayer.this, String.valueOf(player.getTrackDuration()), Toast.LENGTH_LONG).show();
            }
        });
        songSeekBar.setMax((int) soundTotal);

        while (player != null && currentPosition < soundTotal) {
            soundTotal = player.getTrackDuration();
            songSeekBar.setMax((int) soundTotal);
            try {
                Thread.sleep(100);
                currentPosition = player.getPosition();

            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            songSeekBar.setProgress((int) currentPosition);
        }
    }

}
