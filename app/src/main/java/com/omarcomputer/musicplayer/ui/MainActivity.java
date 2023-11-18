package com.omarcomputer.musicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.omarcomputer.musicplayer.R;
import com.omarcomputer.musicplayer.adapter.TrackAdapter;
import com.omarcomputer.musicplayer.databinding.ActivityMainBinding;
import com.omarcomputer.musicplayer.databinding.PlayerBinding;
import com.omarcomputer.musicplayer.model.Track;
import com.omarcomputer.musicplayer.viewmodel.TrackViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements TrackAdapter.Listener {
    ActivityMainBinding binding;
    PlayerBinding player;
    TrackViewModel trackViewModel;
    TrackAdapter trackAdapter;
    List<Track> trackList = new ArrayList<>();
    Track currentTrack;
    MediaPlayer mediaPlayer;

    Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        trackViewModel = new ViewModelProvider(this).get(TrackViewModel.class);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        player = binding.player;

        TimerTask runnable = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        player.playProgress.setProgress(mediaPlayer.getCurrentPosition());
                        player.txtTime.setText(intToTime(mediaPlayer.getCurrentPosition()));
                    }
                });

            }
        };

        timer.scheduleAtFixedRate((TimerTask) runnable, 0, 1000);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if (trackViewModel.currentIndex.getValue() < trackList.size() - 1) {
                trackViewModel.currentIndex.setValue(trackViewModel.currentIndex.getValue() + 1);
            }
        });
        trackAdapter = new TrackAdapter(getApplicationContext(), trackList, this);
        binding.recycler.setAdapter(trackAdapter);
        trackViewModel.trackList.observe(this, tracks -> {
            trackList.clear();
            trackList.addAll(trackViewModel.tracks);
            trackAdapter.notifyDataSetChanged();
        });
        trackViewModel.currentIndex.observe(this, index -> {
            for (int i = 0; i < trackList.size(); i++) {
                trackList.get(i).setIsPlaying(false);
            }
            trackList.get(index).setIsPlaying(true);
            trackAdapter.notifyDataSetChanged();
            currentTrack = trackList.get(index);
            binding.recycler.scrollToPosition(index);

            player.txtTime.setText("00:00");
            player.txtDuration.setText(currentTrack.getTextDuration());
            player.txtTitle.setText(currentTrack.getTitle());
            player.playProgress.setMax((int) currentTrack.getDuration());
            player.playProgress.setProgress(0);


            try {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(getApplicationContext(), currentTrack.getURI());
                mediaPlayer.prepare();
                mediaPlayer.start();
                player.btnPlay.setImageResource(R.drawable.ic_pause);
            } catch (IOException e) {
                // throw new RuntimeException(e);
            }
        });
        player.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    player.btnPlay.setImageResource(R.drawable.ic_play);

                } else {
                    mediaPlayer.start();
                    player.btnPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        player.btnNext.setOnClickListener(v -> {
            if (trackViewModel.currentIndex.getValue() < trackList.size() - 1) {
                trackViewModel.currentIndex.setValue(trackViewModel.currentIndex.getValue() + 1);
            }

        });
        player.btnPrevious.setOnClickListener(v->{
            if (trackViewModel.currentIndex.getValue() > 0) {
                trackViewModel.currentIndex.setValue(trackViewModel.currentIndex.getValue() - 1);
            }
        });

        player.playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        trackViewModel.loadTracks();

    }

    @Override
    public void onPlayPause(int position) {
        trackViewModel.currentIndex.setValue(position);


    }

    String intToTime(int d) {

        int t = d / 1000;
        int s = t % 60;
        int m = t / 60;
        String ss = "" + s;
        String sm = "" + m;
        if (s < 10) ss = "0" + ss;
        if (m < 10) sm = "0" + sm;
        return sm + ":" + ss;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}