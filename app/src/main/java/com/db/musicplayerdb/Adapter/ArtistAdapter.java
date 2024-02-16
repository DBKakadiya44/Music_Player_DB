package com.db.musicplayerdb.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.db.musicplayerdb.AlbumActivity;
import com.db.musicplayerdb.Fragments.ArtistFragment;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.R;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>
{
    MainActivity mainActivity;
    ArrayList artistList;
    ArrayList artistcount;
    public ArtistAdapter(MainActivity mainActivity, ArrayList artistList, ArrayList artistcount) {
        this.mainActivity=mainActivity;
        this.artistList=artistList;
        this.artistcount=artistcount;
    }

    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_artist_fragment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        holder.name.setText(""+artistList.get(position));
        holder.count.setText(artistcount.get(position)+" Songs");

        holder.ccl.setOnClickListener(v -> {
            ArtistFragment.getAudioFilesByArtist(mainActivity, artistList.get(position).toString());

            mainActivity.startActivity(new Intent(mainActivity , AlbumActivity.class).putExtra("from",3).putExtra("name",artistList.get(position).toString()));

        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout ccl;
        TextView name,count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ccl = itemView.findViewById(R.id.itemsong);
            name = itemView.findViewById(R.id.name);
            count = itemView.findViewById(R.id.count);
        }
    }
}
