package com.omarcomputer.musicplayer.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.omarcomputer.musicplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackViewModel extends AndroidViewModel {

    Application application;
    public ArrayList<Track> tracks = new ArrayList<>();

    public MutableLiveData<List<Track>> trackList = new MutableLiveData<>();
    public MutableLiveData<Integer> currentIndex = new MutableLiveData<>();

    public TrackViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void loadTracks() {

        tracks.clear();
        ContentResolver contentResolver = application.getContentResolver();
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

        Cursor cursor = contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
        );

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int uriColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            String name = cursor.getString(nameColumn);
            int duration = cursor.getInt(durationColumn);
            String artist = cursor.getString(artistColumn);
            String album = cursor.getString(albumColumn);
            Uri uri = Uri.parse(cursor.getString(uriColumn));
            Track track = new Track(id, artist, name, album, duration);
            tracks.add(track);
            //Log.i("MyTrackItem",track.toString());

        }

        trackList.setValue(tracks);
        currentIndex.setValue(0);
    }
}
