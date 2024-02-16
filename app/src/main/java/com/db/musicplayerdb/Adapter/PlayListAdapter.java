package com.db.musicplayerdb.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.db.musicplayerdb.AlbumActivity;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.PlayListCode;
import com.db.musicplayerdb.R;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder>
{
    MainActivity mainActivity;
    List<String> namelist;

    public PlayListAdapter(MainActivity mainActivity, List<String> namelist) {
        this.mainActivity = mainActivity;
        this.namelist = namelist;
    }

    @NonNull
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_album_fragment , parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.ViewHolder holder, int position) {

        holder.list.setText("Play List");
        holder.name.setText(""+namelist.get(position));

        holder.ccl.setOnClickListener(v -> {
            PlayListCode.loadplaylistSharedPreferences(mainActivity , namelist.get(position).toString());

            mainActivity.startActivity(new Intent(mainActivity , AlbumActivity.class).putExtra("from",4).putExtra("name",namelist.get(position).toString()));

        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,list;
        ConstraintLayout ccl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            list = itemView.findViewById(R.id.count);
            ccl = itemView.findViewById(R.id.itemsong);
        }
    }
}
