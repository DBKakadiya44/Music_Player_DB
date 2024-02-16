package com.db.musicplayerdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.R;
import java.util.ArrayList;

public class AcAlbumAdapter4 extends RecyclerView.Adapter<AcAlbumAdapter4.ViewHolder>
{
    Context context;
    ArrayList<MusicModel> audioList;

    public AcAlbumAdapter4(Context context, ArrayList<MusicModel> audioList) {
        this.context=context;
        this.audioList=audioList;
    }
    @NonNull
    @Override
    public AcAlbumAdapter4.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_fragment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcAlbumAdapter4.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,time;
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
