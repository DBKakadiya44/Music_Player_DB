package com.db.musicplayerdb.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.db.musicplayerdb.AlbumActivity;
import com.db.musicplayerdb.Fragments.AlbumsFragment;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.AlbumModel;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.R;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>
{
    MainActivity mainActivity;
    ArrayList<AlbumModel> albumwise;
    public AlbumAdapter(MainActivity mainActivity, ArrayList<AlbumModel> albumwise) {
        this.mainActivity=mainActivity;
        this.albumwise=albumwise;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_album_fragment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {

        holder.name.setText(""+albumwise.get(position).getName());
        holder.count.setText(albumwise.get(position).getTotal()+" Songs");

        holder.ccl.setOnClickListener(v -> {
            getAudioFilesForAlbum(mainActivity, albumwise.get(position).getName());
            mainActivity.startActivity(new Intent(mainActivity , AlbumActivity.class).putExtra("from",2).putExtra("name",albumwise.get(position).getName()));
        });

    }

    @Override
    public int getItemCount() {
        return albumwise.size();
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

    public static void getAudioFilesForAlbum(Context context, String albumName) {

        AlbumsFragment.albumwisemusic.clear();

        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST};

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Audio.Media.ALBUM + "=?",
                new String[]{albumName},
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String audioId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String audioTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String audioPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String audioartist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                MusicModel model = new MusicModel(audioTitle, audioId, audioPath,audioartist,1);
                AlbumsFragment.albumwisemusic.add(model);

            } while (cursor.moveToNext());

            cursor.close();
        }

    }

}
