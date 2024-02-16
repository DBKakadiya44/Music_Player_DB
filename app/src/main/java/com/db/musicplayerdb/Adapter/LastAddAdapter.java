package com.db.musicplayerdb.Adapter;

import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentPosition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.ForAll.UpdateBottom;
import com.db.musicplayerdb.Fragments.Activity.LastAddActivity;

import com.db.musicplayerdb.Fragments.SongsFragment;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.MusicActivity;
import com.db.musicplayerdb.R;
import com.db.musicplayerdb.SplashActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LastAddAdapter extends RecyclerView.Adapter<LastAddAdapter.ViewHolder> {
    Context context;
    ArrayList<MusicModel> audioList;
    ArrayList bitmapdata = new ArrayList();

    public LastAddAdapter(Context context, ArrayList<MusicModel> audioList) {
        this.context = context;
        this.audioList = audioList;
    }

    @NonNull
    @Override
    public LastAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastAddAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText("" + audioList.get(position).getName());
        String duration = ThubnailGet.formatDuration(Long.parseLong(audioList.get(position).getDuration()));
        holder.time.setText("" + duration);

        Bitmap thumbnail = ThubnailGet.getAudioThumbnail(audioList.get(position).getPath());
        Bitmap bitmap = ThubnailGet.drawableToBitmap(context, R.drawable.musicicon);
        if (thumbnail != null) {
            bitmapdata.add(position, thumbnail);
        } else {
            bitmapdata.add(position, bitmap);
        }
        holder.image.setImageBitmap((Bitmap) bitmapdata.get(position));

        String getname = SplashActivity.preferences.getString("LastMusicPlay", "");

        if (audioList.get(position).getName().equals(getname)) {
            if (MainActivity.mainmediaPlayer.isPlaying()) {
                holder.playing.setVisibility(View.VISIBLE);
            }
        } else {
            holder.playing.setVisibility(View.INVISIBLE);
        }

        holder.ccl.setOnClickListener(v -> {
            CurrentMusic = audioList;
            CurrentPosition = position;
            context.startActivity(new Intent(context, MusicActivity.class));
            UpdateBottom.updateAllBottom();
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;
        ConstraintLayout ccl;
        ImageView image;
        LottieAnimationView playing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titlename);
            time = itemView.findViewById(R.id.duration);
            ccl = itemView.findViewById(R.id.itemsong);
            image = itemView.findViewById(R.id.imageView6);
            playing = itemView.findViewById(R.id.playing);
        }
    }
}
