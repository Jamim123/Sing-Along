package com.project1.musicapp.singalong;

import java.util.ArrayList;


public class SongAction {
    ArrayList<SongSkeleton> playlist;
    int position = 0;

    public void addSong(SongSkeleton song) {
        playlist.add(song);
    }

    public int getSize() {
        return playlist.size();
    }

    public SongAction() //bujhinai
    {
        playlist = new ArrayList<SongSkeleton>();
    }

    public void Next() {
        position++;
        if (position == playlist.size())
            position = 0;

    }


    public String getNextSongName() {
        int nxtpos = (position+1)%playlist.size();
        return playlist.get(nxtpos).getSongName();
    }

    public void Previous() {
        position--;
        if (position < 0)
            position = playlist.size() - 1;

    }

    public void insert(String name, String artist, String Album, Long url, String back) {
        playlist.add(new SongSkeleton(name, artist, Album, url,back));
    }

    public String getSongName() {
        return playlist.get(position).getSongName();
    }

    public String getArtist() {
        return playlist.get(position).getSongArtist();
    }

    public String getAlbum() {
        return playlist.get(position).getSongAlbum();
    }

    public Long getUrl() {
        return playlist.get(position).getSongId();
    }
    public String getback()
    {
        return playlist.get(position).getBackURL();
    }
}




