package com.project1.musicapp.singalong;


import java.util.TreeMap;

public class SongInfo {
    public static void collectSongs(TreeMap<String, SongAction> data) {
        SongAction sunny = new SongAction();
        data.put("Sunny", sunny);

        SongAction rainy = new SongAction();
        data.put("Rainy", rainy);

        SongAction clouds= new SongAction();
        data.put("Clouds", clouds);

        SongAction Haze = new SongAction();
        data.put("Haze", Haze);

        SongAction nightsky = new SongAction();
        data.put("Nightsky", nightsky);
    }
}
