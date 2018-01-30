package com.project1.musicapp.singalong;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AddSongs extends AppCompatActivity {
    EditText weather;
    EditText language;
    EditText songId;
    EditText songName;
    EditText songArtist;
    EditText songAlbum;
    EditText backgroundImageURL;
    Button uploadSong;
    Button getSongData;
    EditText genreName;


    String title;
    String artistNameText;
    String albumTitleText;
    String backgroundImageUrlText;
    String weatherData;
    String languageData;
    String id;
    String genre;

    class RetrieveSongs extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {


            JSONObject songData = new JSONObject();
            try {
                final URL apiUrl = new URL("https://api.deezer.com/track/" + id);
                HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                final StringBuilder builder = new StringBuilder();


                String inputString;

                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddSongs.this, apiUrl.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                songData = new JSONObject(builder.toString());
                title = songData.getString("title");
                JSONObject artist = (JSONObject) songData.get("artist");
                artistNameText = artist.getString("name");
                JSONObject album = (JSONObject) songData.get("album");
                albumTitleText = album.getString("title");
                backgroundImageUrlText = album.getString("cover_big");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return songData;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            songName.setText(title);
            songArtist.setText(artistNameText);
            songAlbum.setText(albumTitleText);
            backgroundImageURL.setText(backgroundImageUrlText);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_songs);

        weather = findViewById(R.id.weather);
        language = findViewById(R.id.language);
        songId = findViewById(R.id.songId);
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        songAlbum = findViewById(R.id.songAlbum);
        backgroundImageURL = findViewById(R.id.bgUrl);
        uploadSong = findViewById(R.id.uploadSong);
        getSongData = findViewById(R.id.getSongData);
        genreName=findViewById(R.id.genre);


        getSongData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = songId.getText().toString();
                new RetrieveSongs().execute(id);
            }
        });

        uploadSong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                weatherData = weather.getText().toString();
                languageData = language.getText().toString();
                genre=genreName.getText().toString();

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Weather");
                DatabaseReference songCollection = database.child(weatherData).child("Songs").child(languageData);
                String key = songCollection.push().getKey();
                DatabaseReference newSong = songCollection.child(key);

                SongSkeleton skeleton = new SongSkeleton(title, artistNameText, albumTitleText, Long.parseLong(id), backgroundImageUrlText);
                newSong.child("songData").setValue(skeleton);
                newSong.child("genre").setValue(genre);
                Toast.makeText(AddSongs.this, String.valueOf(genre), Toast.LENGTH_LONG).show();
            }
        });
    }


}
