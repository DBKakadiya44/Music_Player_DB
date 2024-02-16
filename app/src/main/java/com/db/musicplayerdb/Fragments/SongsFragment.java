package com.db.musicplayerdb.Fragments;

import static com.db.musicplayerdb.ForAll.UpdateBottom.updateAllBottom;
import static com.db.musicplayerdb.MainActivity.*;
import static com.db.musicplayerdb.Model.CurrentPlayArray.*;
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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.musicplayerdb.Adapter.SongAdapter;
import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.ForAll.UpdateBottom;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.MusicActivity;
import com.db.musicplayerdb.QueueActivity;
import com.db.musicplayerdb.R;
import com.db.musicplayerdb.SplashActivity;
import com.db.musicplayerdb.databinding.FragmentSongsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SongsFragment extends Fragment {

    public static FragmentSongsBinding binding;
    public static MainActivity mainActivity;
    public static Context context;
    public static ArrayList<MusicModel> musicdata = new ArrayList<>();
    TextView f1;
    public static Boolean isCreated = false;
    public static SongAdapter adapter;
    public static int newmostplay = 0;


    public SongsFragment(MainActivity mainActivity, TextView f1) {
        this.mainActivity = mainActivity;
        this.f1 = f1;
        getallmusic(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(getLayoutInflater());

        f1.setTextColor(getActivity().getColor(R.color.maincolor));
        f1.setTextSize(16);
        isCreated = true;

        UpdateBottom.updateAllBottom();
        context = mainActivity;

        int mostplay = SplashActivity.preferences.getInt("mostplay", 0);
        if (mostplay == 1) {
            MostplayCode.loadmostplaylistSharedPreferences(context);
            checkfornewsong();
        } else {
            MostPlayMusic = SongsFragment.musicdata;
        }

        adapter = new SongAdapter(mainActivity, musicdata);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        binding.songrecycler.setLayoutManager(manager);
        binding.songrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return binding.getRoot();

    }

    private void checkfornewsong() {
        if (musicdata.size() == MostPlayMusic.size()) {
        } else {
            MostPlayMusic = musicdata;
            MostplayCode.mostplaylistdata(context, MostPlayMusic);
        }
    }

    public static void checkcurrentmusic() {

        if (CurrentMusic.isEmpty()) {
            binding.constraintLayout7.setVisibility(View.GONE);
        } else {
            binding.constraintLayout7.setVisibility(View.VISIBLE);

            binding.constraintLayout7.setOnClickListener(v -> {
                context.startActivity(new Intent(context, MusicActivity.class));
            });

            binding.bname.setText("" + CurrentMusic.get(CurrentPosition).getName());
            //set thubnail
            Bitmap thumbnail = ThubnailGet.getAudioThumbnail(CurrentMusic.get(CurrentPosition).getPath());
            Bitmap drawable = ThubnailGet.drawableToBitmap(context, R.drawable.musicicon);
            if (thumbnail != null) {
                binding.bthumbnail.setImageBitmap(thumbnail);
            } else {
                binding.bthumbnail.setImageBitmap(drawable);
            }
            //set play pause button
            if (mainmediaPlayer.isPlaying()) {
                binding.bplay.setVisibility(View.INVISIBLE);
                binding.bpause.setVisibility(View.VISIBLE);
            } else {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
            }
            //handle play pause click
            binding.bplay.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.INVISIBLE);
                binding.bpause.setVisibility(View.VISIBLE);

                if (SplashActivity.FirstPlay == 0) {
                    try {
                        CurrentPlayingTime.progress = 0;

                        mainmediaPlayer.reset();
                        mainmediaPlayer.setDataSource(CurrentMusic.get(CurrentPosition).getPath());
                        mainmediaPlayer.prepare();
                        mainmediaPlayer.start();

                        editor.putString("LastMusicPlay", CurrentMusic.get(CurrentPosition).getName());
                        editor.commit();

                        LastPlayCode.Lastdata(context, CurrentMusic);

                        SplashActivity.FirstPlay = 1;


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
                } else {
                    mainmediaPlayer.start();
                }

                UpdateBottom.updateAllBottom();
                adapter = new SongAdapter(mainActivity, musicdata);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                binding.songrecycler.setLayoutManager(manager);
                binding.songrecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });

            binding.bpause.setOnClickListener(v -> {

                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();


                UpdateBottom.updateAllBottom();
                adapter = new SongAdapter(mainActivity, musicdata);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
                binding.songrecycler.setLayoutManager(manager);
                binding.songrecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context, QueueActivity.class));
            });

        }
        // for music animation update
        adapter = new SongAdapter(mainActivity, musicdata);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        binding.songrecycler.setLayoutManager(manager);
        binding.songrecycler.setAdapter(adapter);
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

    void getallmusic(Context context) {

        musicdata.clear();

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST
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
                    musicdata.add(musicModel);
                }
            } catch (Exception e) {
                Log.e("AudioFileUtils", "Error retrieving audio files", e);
            } finally {
                cursor.close();
            }
        }

    }


}