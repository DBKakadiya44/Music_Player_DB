package com.db.musicplayerdb.Fragments.Activity;

import static com.db.musicplayerdb.ForAll.UpdateBottom.updateAllBottom;
import static com.db.musicplayerdb.MainActivity.mainmediaPlayer;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentPosition;
import static com.db.musicplayerdb.Model.CurrentPlayArray.MostPlayMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.RecentPlayMusic;
import static com.db.musicplayerdb.SplashActivity.CurrentMode;
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

import com.db.musicplayerdb.Adapter.RecentAdapter;
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
import com.db.musicplayerdb.databinding.ActivityRecentBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RecentActivity extends AppCompatActivity {
    public static ActivityRecentBinding binding;
    public static Context context;
    public static ArrayList<MusicModel> musicdata = new ArrayList<>();
    public static Boolean isCreated = false;
    public static Boolean isplaying = false;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.c2));

        context = RecentActivity.this;
        musicdata = CurrentPlayArray.RecentPlayMusic;
        Collections.reverse(musicdata);

        isCreated = true;
        UpdateBottom.updateAllBottom();


        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        if (musicdata.isEmpty()) {
            binding.textView5.setText("0 Songs");
            binding.noText.setVisibility(View.VISIBLE);
        } else {
            binding.textView5.setText(musicdata.size() + " Songs");
            binding.noText.setVisibility(View.INVISIBLE);

            RecentAdapter adapter = new RecentAdapter(RecentActivity.this, musicdata);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(RecentActivity.this);
            binding.recentrecycler.setLayoutManager(manager);
            binding.recentrecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        binding.playall.setOnClickListener(v -> {
            binding.playall.setVisibility(View.INVISIBLE);
            binding.pauseall.setVisibility(View.VISIBLE);
            checkAllMusic();
            UpdateBottom.updateAllBottom();

        });
        binding.pauseall.setOnClickListener(v -> {
            binding.playall.setVisibility(View.VISIBLE);
            binding.pauseall.setVisibility(View.INVISIBLE);

            if (mainmediaPlayer.isPlaying()) {
                mainmediaPlayer.pause();
            }
            UpdateBottom.updateAllBottom();
        });

        binding.shuffle.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.INVISIBLE);
            binding.loop.setVisibility(View.VISIBLE);
            binding.oneloop.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode = 2;
        });
        binding.loop.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.INVISIBLE);
            binding.loop.setVisibility(View.INVISIBLE);
            binding.oneloop.setVisibility(View.VISIBLE);

            mainmediaPlayer.setLooping(true);
            SplashActivity.CurrentMode = 3;
        });
        binding.oneloop.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.VISIBLE);
            binding.loop.setVisibility(View.INVISIBLE);
            binding.oneloop.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode = 1;
        });

    }

    private void checkAllMusic() {
        if (isplaying == true) {
            mainmediaPlayer.start();
        } else {
            CurrentMusic = musicdata;
            CurrentPosition = 0;
            startAllMusic();
        }
    }

    private void startAllMusic() {

        if (mainmediaPlayer.isPlaying()) {
            mainmediaPlayer.stop();
            mainmediaPlayer.reset();
        }
        isplaying = true;

        if (mainmediaPlayer.isLooping()) {

            try {
                mainmediaPlayer.reset();
                mainmediaPlayer.setDataSource(musicdata.get(currentIndex).getPath());
                mainmediaPlayer.prepare();
                mainmediaPlayer.start();

                SplashActivity.editor.putString("LastMusicPlay", musicdata.get(currentIndex).getName());
                SplashActivity.editor.commit();

                LastPlayCode.Lastdata(context, CurrentMusic);

                CurrentPosition = currentIndex;

                RecentAdapter adapter = new RecentAdapter(RecentActivity.this, musicdata);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(RecentActivity.this);
                binding.recentrecycler.setLayoutManager(manager);
                binding.recentrecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                UpdateBottom.updateAllBottom();

                mainmediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        startAllMusic();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            if (currentIndex < musicdata.size()) {
                try {
                    mainmediaPlayer.reset();
                    mainmediaPlayer.setDataSource(musicdata.get(currentIndex).getPath());
                    mainmediaPlayer.prepare();
                    mainmediaPlayer.start();

                    SplashActivity.editor.putString("LastMusicPlay", musicdata.get(currentIndex).getName());
                    SplashActivity.editor.commit();

                    LastPlayCode.Lastdata(context, CurrentMusic);

                    CurrentPosition = currentIndex;

                    RecentAdapter adapter = new RecentAdapter(RecentActivity.this, musicdata);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(RecentActivity.this);
                    binding.recentrecycler.setLayoutManager(manager);
                    binding.recentrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    UpdateBottom.updateAllBottom();

                    mainmediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            currentIndex++;
                            startAllMusic();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                currentIndex = 0;
                UpdateBottom.updateAllBottom();
                binding.playall.setVisibility(View.VISIBLE);
                isplaying = false;
                mainmediaPlayer.stop();
                binding.pauseall.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void checkcurrentmusic() {

        if (CurrentMusic.isEmpty()) {
            binding.constraintLayout7.setVisibility(View.GONE);
        } else {
            binding.constraintLayout7.setVisibility(View.VISIBLE);

            binding.constraintLayout7.setOnClickListener(v -> {
                context.startActivity(new Intent(context , MusicActivity.class));
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
                if (isplaying) {
                    binding.playall.setVisibility(View.INVISIBLE);
                    binding.pauseall.setVisibility(View.VISIBLE);
                }

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
                if (musicdata.isEmpty()) {
                    binding.textView5.setText("0 Songs");
                    binding.noText.setVisibility(View.VISIBLE);
                } else {
                    RecentAdapter adapter = new RecentAdapter(context, musicdata);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
                    binding.recentrecycler.setLayoutManager(manager);
                    binding.recentrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                if (isplaying) {
                    binding.playall.setVisibility(View.VISIBLE);
                    binding.pauseall.setVisibility(View.INVISIBLE);
                }

                UpdateBottom.updateAllBottom();
                if (musicdata.isEmpty()) {
                    binding.textView5.setText("0 Songs");
                    binding.noText.setVisibility(View.VISIBLE);
                } else {
                    RecentAdapter adapter = new RecentAdapter(context, musicdata);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
                    binding.recentrecycler.setLayoutManager(manager);
                    binding.recentrecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context, QueueActivity.class));
            });

        }
        // for music animation update
        if (musicdata.isEmpty()) {
            binding.textView5.setText("0 Songs");
            binding.noText.setVisibility(View.VISIBLE);
        } else {
            RecentAdapter adapter = new RecentAdapter(context, musicdata);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
            binding.recentrecycler.setLayoutManager(manager);
            binding.recentrecycler.setAdapter(adapter);
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

        MostplayCode.mostplaylistdata(context, MostPlayMusic);
        SplashActivity.editor.putInt("mostplay", 1);
        SplashActivity.editor.commit();


    }

    @Override
    public void onBackPressed() {
        Collections.reverse(musicdata);
        super.onBackPressed();
    }
}