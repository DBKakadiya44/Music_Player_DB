package com.db.musicplayerdb.Fragments;

import static com.db.musicplayerdb.ForAll.UpdateBottom.updateAllBottom;
import static com.db.musicplayerdb.MainActivity.mainmediaPlayer;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentPosition;
import static com.db.musicplayerdb.Model.CurrentPlayArray.MostPlayMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.RecentPlayMusic;
import static com.db.musicplayerdb.SplashActivity.CurrentMode;
import static com.db.musicplayerdb.SplashActivity.editor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.db.musicplayerdb.Adapter.FolderAdapter;
import com.db.musicplayerdb.Adapter.SongAdapter;
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
import com.db.musicplayerdb.databinding.FragmentFoldersBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FoldersFragment extends Fragment {
    public static FragmentFoldersBinding binding;
    public static MainActivity mainActivity;
    public static Context context;
    public static Boolean isCreated = false;
    public static ArrayList folderdata = new ArrayList();
    public static ArrayList foldercount = new ArrayList();
    public static ArrayList<MusicModel> folderwisemusic = new ArrayList();
    TextView f3;

    public FoldersFragment(MainActivity mainActivity, TextView f3) {
        this.mainActivity = mainActivity;
        this.f3 = f3;
        getfolders(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoldersBinding.inflate(getLayoutInflater());

        f3.setTextColor(getActivity().getColor(R.color.maincolor));
        f3.setTextSize(16);
        isCreated = true;

        FolderAdapter adapter = new FolderAdapter(mainActivity, folderdata);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        binding.folderrecycler.setLayoutManager(manager);
        binding.folderrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        UpdateBottom.updateAllBottom();
        context = mainActivity;

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
                FolderAdapter adapter = new FolderAdapter(mainActivity, folderdata);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                binding.folderrecycler.setLayoutManager(manager);
                binding.folderrecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                UpdateBottom.updateAllBottom();
                FolderAdapter adapter = new FolderAdapter(mainActivity, folderdata);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                binding.folderrecycler.setLayoutManager(manager);
                binding.folderrecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context , QueueActivity.class));
            });

        }
        // for music animation update
        FolderAdapter adapter = new FolderAdapter(mainActivity, folderdata);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        binding.folderrecycler.setLayoutManager(manager);
        binding.folderrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

    private static void getfolders(Context context) {

        String[] projection = {
                MediaStore.Audio.Albums.ALBUM
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);

                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(dataIndex);

                    if (folderdata.isEmpty()) {
                        folderdata.add(filePath);
                        getAudioFileCountByFolderName(context, filePath);
                    } else {
                        if (folderdata.contains(filePath)) {

                        } else {
                            folderdata.add(filePath);
                            getAudioFileCountByFolderName(context, filePath);
                        }
                    }

                }
            } catch (Exception e) {
                Log.e("AudioFileUtils", "Error retrieving audio files", e);
            } finally {
                cursor.close();
            }
        }

    }

    public static void getAudioFileCountByFolderName(Context context, String folderName)
    {

        int audioFileCount = 0;

        String[] projection = {
                MediaStore.Audio.Media.DATA
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                MediaStore.Audio.Media.DATA + " like ?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null) {
            try {
                audioFileCount = cursor.getCount();
            } catch (Exception e) {
                Log.e("AudioFolderUtils", "Error retrieving audio file count", e);
            } finally {
                cursor.close();
            }
        }
        foldercount.add(audioFileCount);
    }

    public static void getAudioFilesByFolderName(Context context, String folderName) {

        folderwisemusic.clear();

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                MediaStore.Audio.Media.DATA + " like ?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int name = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(columnIndex);
                    String filename = cursor.getString(name);
                    String fileduration = cursor.getString(duration);
                    String fileartist = cursor.getString(artist);

                    MusicModel musicModel = new MusicModel(filename, filePath, fileduration, fileartist, 1);
                    folderwisemusic.add(musicModel);
                }
            } catch (Exception e) {
                Log.e("AudioFolderUtils", "Error retrieving audio files", e);
            } finally {
                cursor.close();
            }
        }
    }

}