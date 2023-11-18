package com.omarcomputer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omarcomputer.musicplayer.R;
import com.omarcomputer.musicplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private Context context;
    List<Track> trackList;
    Listener listener;

    public interface Listener {
        void onPlayPause(int position);
    }

    public TrackAdapter(Context context,List<Track> tracks, Listener listener) {
        trackList = tracks;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.txtTitle.setText(track.getTitle());
        holder.txtDuration.setText("" + track.getTextDuration());
        holder.itemView.setOnClickListener(v->{
            listener.onPlayPause(position);
            Log.i("MyTrackItem","playing : " + track.getTitle());

        });
        if (track.isPlaying()) {
            holder.itemLayout.setBackgroundResource(R.color.black);
        } else {
            holder.itemLayout.setBackgroundResource(R.color.colorPrimary );
        }


    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDuration;
        ImageButton btnPlayPause;
        LinearLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            btnPlayPause = itemView.findViewById(R.id.btnPlayPause);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
