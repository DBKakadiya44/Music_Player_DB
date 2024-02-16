package com.db.musicplayerdb.Adapter;

import static com.db.musicplayerdb.ForAll.ThubnailGet.*;

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

import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.Fragments.SongsFragment;
import com.db.musicplayerdb.MainActivity;

import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    MainActivity mainActivity;
    ArrayList<MusicModel> audioList;
    ArrayList bitmapdata = new ArrayList();
    public static ArrayList items = new ArrayList<>();
    int itemget = 0;
    public SelectAdapter(MainActivity mainActivity, ArrayList<MusicModel> audioList) {
        this.mainActivity = mainActivity;
        this.audioList = audioList;
        items.clear();
    }

    @NonNull
    @Override
    public SelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAdapter.ViewHolder holder, int position) {

        if(itemget==0){
            for (int i = 0; i < SongsFragment.musicdata.size(); i++) {
                items.add(false);
            }
            itemget =1;
        }

        holder.name.setText("" + audioList.get(position).getName());
        String duration = formatDuration(Long.parseLong(audioList.get(position).getDuration()));
        holder.time.setText("" + duration);

        if (items.get(position).equals(false)) {
            holder.ccl.setBackgroundResource(R.color.black);
        } else {
            holder.ccl.setBackgroundResource(R.drawable.main_bg);
        }

        holder.ccl.setOnClickListener(v -> {
            if (items.get(position).equals(false)) {
                holder.ccl.setBackgroundResource(R.drawable.selected_bg);
                items.remove(position);
                items.add(position, true);
            } else {
                holder.ccl.setBackgroundResource(R.color.black);
                items.remove(position);
                items.add(position, false);
            }

            if (items.get(position).equals(false)) {

            } else {

            }
        });

        Bitmap thumbnail = getAudioThumbnail(audioList.get(position).getPath());
        Bitmap bitmap = drawableToBitmap(mainActivity,R.drawable.musicicon);

        if (thumbnail != null) {
            bitmapdata.add(position , thumbnail);
        } else {
            bitmapdata.add(position , bitmap);
        }

        holder.image.setImageBitmap((Bitmap) bitmapdata.get(position));


    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;
        ImageView without, with ,image;
        ConstraintLayout ccl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.songname);
            time = itemView.findViewById(R.id.songduration);
            with = itemView.findViewById(R.id.imageView8);
            without = itemView.findViewById(R.id.imageView5);
            ccl = itemView.findViewById(R.id.itemselect);
            image = itemView.findViewById(R.id.imageView6);
        }
    }
}
