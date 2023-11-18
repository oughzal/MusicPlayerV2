package com.omarcomputer.musicplayer.model;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

public class Track {
    long id;
    String artist;
    String title;
    String album;
    long duration;



    boolean fav = false;
    boolean isPlaying = false;

    public Track() {
        super();
    }

    public Track(long id, String artist, String title, String album, long duration) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.duration = duration;
    }

    public Uri getURI() {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }

    public String getTextDuration() {
        Long d = duration;
        d = d / 1000;
        long m = d / 60;
        long s = d % 60;
        String sm = "" + m;
        if (m < 10) sm = "0" + sm;
        String ss = "" + s;
        if (s < 10) ss = "0" + ss;
        return sm +":" +ss;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
    public void setIsPlaying(boolean v) {
       isPlaying =v;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", fav=" + fav +
                ", isPlaying=" + isPlaying +
                '}';
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
