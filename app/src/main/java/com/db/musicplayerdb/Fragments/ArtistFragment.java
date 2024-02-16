package com.db.musicplayerdb.Fragments;

import static com.db.musicplayerdb.ForAll.UpdateBottom.updateAllBottom;
import static com.db.musicplayerdb.MainActivity.mainmediaPlayer;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentPosition;
import static com.db.musicplayerdb.Model.CurrentPlayArray.MostPlayMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.RecentPlayMusic;
import static com.db.musicplayerdb.SplashActivity.CurrentMode;
import static com.db.musicplayerdb.SplashActivity.editor;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.musicplayerdb.Adapter.ArtistAdapter;
import com.db.musicplayerdb.Adapter.FolderAdapter;
import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.ForAll.UpdateBottom;
import com.db.musicplayerdb.MainActivity;

import com.db.musicplayerdb.Model.CurrentPlayArray;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.MusicActivity;
import com.db.musicplayerdb.QueueActivity;
import com.db.musicplayerdb.R;
import com.db.musicplayerdb.SplashActivity;
import com.db.musicplayerdb.databinding.FragmentArtistBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ArtistFragment extends Fragment {

    public static FragmentArtistBinding binding;
    public static MainActivity mainActivity;
    public static Context context;
    public static ArrayList artistList = new ArrayList<>();
    public static ArrayList artistcount = new ArrayList();
    public static ArrayList<MusicModel> artistwisemusic = new ArrayList();
    public static Boolean isCreated = false;
    TextView f4;

    public ArtistFragment(MainActivity mainActivity, TextView f4) {
        this.mainActivity = mainActivity;
        this.f4 = f4;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistBinding.inflate(getLayoutInflater());

        f4.setTextColor(getActivity().getColor(R.color.maincolor));
        f4.setTextSize(16);

        context = mainActivity;
        isCreated = true;

        UpdateBottom.updateAllBottom();
        getAllAudioArtists(context);

        if(!artistList.isEmpty()) {
            ArtistAdapter adapter = new ArtistAdapter(mainActivity, artistList, artistcount);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
            binding.artistrecycler.setLayoutManager(manager);
            binding.artistrecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return binding.getRoot();
    }

    public static void checkcurrentmusic() {

        if (CurrentMusic.isEmpty()) {
            binding.constraintLayout7.setVisibility(View.GONE);
        }
        else {
            binding.constraintLayout7.setVisibility(View.VISIBLE);

            binding.constraintLayout7.setOnClickListener(v -> {
                context.startActivity(new Intent(context , MusicActivity.class));
            });

            binding.bname.setText(""+ CurrentMusic.get(CurrentPosition).getName());
            //set thubnail
            Bitmap thumbnail = ThubnailGet.getAudioThumbnail(CurrentMusic.get(CurrentPosition).getPath());
            Bitmap drawable = ThubnailGet.drawableToBitmap(context, R.drawable.musicicon);
            if(thumbnail!=null){
                binding.bthumbnail.setImageBitmap(thumbnail);
            }else {
                binding.bthumbnail.setImageBitmap(drawable);
            }
            //set play pause button
            if(mainmediaPlayer.isPlaying()){
                binding.bplay.setVisibility(View.INVISIBLE);
                binding.bpause.setVisibility(View.VISIBLE);
            }else {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
            }
            //handle play pause click
            binding.bplay.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.INVISIBLE);
                binding.bpause.setVisibility(View.VISIBLE);

                if(SplashActivity.FirstPlay==0){
                    try {
                        CurrentPlayingTime.progress = 0;

                        mainmediaPlayer.reset();
                        mainmediaPlayer.setDataSource(CurrentMusic.get(CurrentPosition).getPath());
                        mainmediaPlayer.prepare();
                        mainmediaPlayer.start();

                        editor.putString("LastMusicPlay",CurrentMusic.get(CurrentPosition).getName());
                        editor.commit();

                        LastPlayCode.Lastdata(context , CurrentMusic);

                        SplashActivity.FirstPlay=1;

                        mainmediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (CurrentPosition < CurrentMusic.size() - 1) {
                                    if (CurrentMusic.size() != 1) {

                                        if (CurrentMode == 1) {
                                            Random random = new Random();
                                            int randomNumber = random.nextInt(CurrentMusic.size());
                                            CurrentPosition = randomNumber;
                                            playMusics();
                                            updateAllBottom();
                                        }
                                        if (CurrentMode == 2) {
                                            CurrentPosition++;
                                            playMusics();
                                            updateAllBottom();
                                        }
                                        if (CurrentMode == 3) {
                                            playMusics();
                                            updateAllBottom();
                                        }
                                    }
                                }
                            }
                        });

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    mainmediaPlayer.start();
                }

                UpdateBottom.updateAllBottom();
                if(!artistList.isEmpty()) {
                    ArtistAdapter adapter = new ArtistAdapter(mainActivity, artistList, artistcount);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                    binding.artistrecycler.setLayoutManager(manager);
                    binding.artistrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                UpdateBottom.updateAllBottom();
                if(!artistList.isEmpty()) {
                    ArtistAdapter adapter = new ArtistAdapter(mainActivity, artistList, artistcount);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                    binding.artistrecycler.setLayoutManager(manager);
                    binding.artistrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context , QueueActivity.class));
            });

        }
        // for music animation update
        if(!artistList.isEmpty()) {
            ArtistAdapter adapter = new ArtistAdapter(mainActivity, artistList, artistcount);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
            binding.artistrecycler.setLayoutManager(manager);
            binding.artistrecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private static void playMusics() {
        CurrentPlayingTime.progress = 0;


        try {
            mainmediaPlayer.reset();
            mainmediaPlayer.setDataSource(CurrentMusic.get(CurrentPosition).getPath());
            mainmediaPlayer.prepare();
            mainmediaPlayer.start();

            editor.putString("LastMusicPlay", CurrentMusic.get(CurrentPosition).getName());
            editor.commit();

            LastPlayCode.Lastdata(context, CurrentMusic);

            playAudio(CurrentMusic.get(CurrentPosition));

            MusicModel musicModel = new MusicModel(CurrentMusic.get(CurrentPosition).getName(), CurrentMusic.get(CurrentPosition).getPath(), CurrentMusic.get(CurrentPosition).getDuration(), CurrentMusic.get(CurrentPosition).getArtist(), CurrentMusic.get(CurrentPosition).getPlayCount());

            if (RecentPlayMusic.isEmpty()) {
                RecentPlayMusic.add(musicModel);
            } else {
                for (int i = 0; i < RecentPlayMusic.size(); i++) {
                    if (RecentPlayMusic.get(i).getName().equals(CurrentMusic.get(CurrentPosition).getName())) {
                        RecentPlayMusic.remove(i);
                    }
                }
                RecentPlayMusic.add(musicModel);
            }

            mainmediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (CurrentPosition < CurrentMusic.size() - 1) {
                        if (CurrentMusic.size() != 1) {

                            if (CurrentMode == 1) {
                                Random random = new Random();
                                int randomNumber = random.nextInt(CurrentMusic.size());
                                CurrentPosition = randomNumber;
                                playMusics();
                                updateAllBottom();
                            }
                            if (CurrentMode == 2) {
                                CurrentPosition++;
                                playMusics();
                                updateAllBottom();
                            }
                            if (CurrentMode == 3) {
                                playMusics();
                                updateAllBottom();
                            }
                        }
                    }

                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void playAudio(MusicModel audio) {

        String name1 = audio.getName();

        for (int i = 0; i < MostPlayMusic.size(); i++) {
            String name2 = MostPlayMusic.get(i).getName();

            if (name1.equals(name2)) {
                MostPlayMusic.get(i).incrementPlayCount();
            }
        }

        MostplayCode.mostplaylistdata(mainActivity, MostPlayMusic);
        SplashActivity.editor.putInt("mostplay", 1);
        SplashActivity.editor.commit();


    }

    public static void getAllAudioArtists(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                MediaStore.Audio.Media.ARTIST
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        try (Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                int artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                do {
                    String artist = cursor.getString(artistColumnIndex);

                    if (!artistList.contains(artist)) {
                        artistList.add(artist);
                        getartistcount(context, artist);
                    }
                } while (cursor.moveToNext());
            }
        }

    }

    private static void getartistcount(Context context, String artist) {

        int numberOfSongs = 0;
        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                MediaStore.Audio.Media.ARTIST + "=?";
        String[] selectionArgs = {artist};

        try (Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor != null) {
                numberOfSongs = cursor.getCount();
            }
        }
        artistcount.add(numberOfSongs);

    }

    public static void getAudioFilesByArtist(Context context, String artistName) {

        artistwisemusic.clear();
        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST
        };

        String selection = MediaStore.Audio.Media.ARTIST + "=?";
        String[] selectionArgs = {artistName};

        try (Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int dataname = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int datatime = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int dataartist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                do {
                    String name = cursor.getString(dataname);
                    String path = cursor.getString(idColumnIndex);
                    String time = cursor.getString(datatime);
                    String artist = cursor.getString(dataartist);

                    MusicModel model = new MusicModel(name, path, time,artist,1);
                    artistwisemusic.add(model);

                } while (cursor.moveToNext());
            }
        }

    }

}