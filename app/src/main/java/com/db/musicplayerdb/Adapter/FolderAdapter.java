package com.db.musicplayerdb.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.db.musicplayerdb.AlbumActivity;
import com.db.musicplayerdb.Fragments.FoldersFragment;
import com.db.musicplayerdb.MainActivity;

import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.R;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    MainActivity mainActivity;
    ArrayList folderdata;

    public FolderAdapter(MainActivity mainActivity, ArrayList folderdata) {
        this.mainActivity = mainActivity;
        this.folderdata = folderdata;
    }

    @NonNull
    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_folder_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.ViewHolder holder, int position) {
        holder.name.setText("" + folderdata.get(position));
        holder.count.setText(FoldersFragment.foldercount.get(position) +" Audios");

        holder.ccl.setOnClickListener(v -> {
            FoldersFragment.getAudioFilesByFolderName(mainActivity, folderdata.get(position).toString());

            mainActivity.startActivity(new Intent(mainActivity , AlbumActivity.class).putExtra("from",1).putExtra("name",folderdata.get(position).toString()));

        });
    }

    @Override
    public int getItemCount() {
        return folderdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, count;
        ConstraintLayout ccl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foldername);
            count = itemView.findViewById(R.id.count);
            ccl = itemView.findViewById(R.id.itemsong);
        }
    }
}
