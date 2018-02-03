package com.project1.musicapp.singalong;


import java.util.TreeMap;

public class SongInfo {
    public static void collectSongs(TreeMap<String, SongAction> data) {
        SongAction Sunny = new SongAction();
        data.put("Sunny", Sunny);

        SongAction Rainy = new SongAction();
        data.put("Rainy", Rainy);

        SongAction Cloudy= new SongAction();
        data.put("Cloudy", Cloudy);

        SongAction Windy = new SongAction();
        data.put("Windy", Windy);

        SongAction Snowy = new SongAction();
        data.put("Snowy", Snowy);
    }
}
