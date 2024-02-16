package com.db.musicplayerdb;

import static com.db.musicplayerdb.ForAll.UpdateBottom.updateAllBottom;
import static com.db.musicplayerdb.MainActivity.mainmediaPlayer;
import static com.db.musicplayerdb.Model.CurrentPlayArray.*;
import static com.db.musicplayerdb.SplashActivity.*;
import static com.db.musicplayerdb.SplashActivity.editor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.db.musicplayerdb.Adapter.QueueAdapter;
import com.db.musicplayerdb.Adapter.SongAdapter;
import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.ForAll.UpdateBottom;

import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.databinding.ActivityQueueBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QueueActivity extends AppCompatActivity {
    public static ActivityQueueBinding binding;
    public static ArrayList<MusicModel> musicdata = new ArrayList<>();
    public static Context context;
    public static Boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        context = this;
        isCreated = true;

        UpdateBottom.updateAllBottom();
        musicdata = CurrentMusic;

        binding.imageView8.setOnClickListener(v -> {
            onBackPressed();
        });

        if(CurrentMode==1){
            binding.relimage.setImageResource(R.drawable.suffle);
            binding.reltext.setText("Shuffle All");
        }
        if(CurrentMode==2){
            binding.relimage.setImageResource(R.drawable.loop);
            binding.reltext.setText("Loop All");
        }
        if(CurrentMode==3){
            binding.relimage.setImageResource(R.drawable.oneloop);
            binding.reltext.setText("Loop this song");
        }

        QueueAdapter adapter = new QueueAdapter(QueueActivity.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(QueueActivity.this);
        binding.queuerecycler.setLayoutManager(manager);
        binding.queuerecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

                if(FirstPlay==0){
                    try {
                        CurrentPlayingTime.progress = 0;

                        mainmediaPlayer.reset();
                        mainmediaPlayer.setDataSource(CurrentMusic.get(CurrentPosition).getPath());
                        mainmediaPlayer.prepare();
                        mainmediaPlayer.start();

                        editor.putString("LastMusicPlay",CurrentMusic.get(CurrentPosition).getName());
                        editor.commit();

                        LastPlayCode.Lastdata(context , CurrentMusic);
                        FirstPlay=1;

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
                QueueAdapter adapter = new QueueAdapter(context);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
                binding.queuerecycler.setLayoutManager(manager);
                binding.queuerecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                UpdateBottom.updateAllBottom();
                QueueAdapter adapter = new QueueAdapter(context);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
                binding.queuerecycler.setLayoutManager(manager);
                binding.queuerecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            //current playing queue activity
            /*binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context , QueueActivity.class));
            });*/

        }
        // for music animation update
        QueueAdapter adapter = new QueueAdapter(context);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        binding.queuerecycler.setLayoutManager(manager);
        binding.queuerecycler.setAdapter(adapter);
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

        MostplayCode.mostplaylistdata(context, MostPlayMusic);
        SplashActivity.editor.putInt("mostplay", 1);
        SplashActivity.editor.commit();


    }

}