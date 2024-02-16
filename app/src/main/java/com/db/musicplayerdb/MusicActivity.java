package com.db.musicplayerdb;

import static com.db.musicplayerdb.ForAll.UpdateBottom.*;
import static com.db.musicplayerdb.MainActivity.*;
import static com.db.musicplayerdb.Model.CurrentPlayArray.*;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentMusic;
import static com.db.musicplayerdb.Model.CurrentPlayArray.CurrentPosition;
import static com.db.musicplayerdb.SplashActivity.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.Toast;

import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.ForAll.UpdateBottom;
import com.db.musicplayerdb.Fragments.Activity.FavoriteActivity;
import com.db.musicplayerdb.Fragments.Activity.LastAddActivity;
import com.db.musicplayerdb.Fragments.Activity.MostPlayActivity;
import com.db.musicplayerdb.Fragments.Activity.RecentActivity;
import com.db.musicplayerdb.Fragments.AlbumsFragment;
import com.db.musicplayerdb.Fragments.ArtistFragment;
import com.db.musicplayerdb.Fragments.FoldersFragment;
import com.db.musicplayerdb.Fragments.PlaylistFragment;
import com.db.musicplayerdb.Fragments.SongsFragment;

import com.db.musicplayerdb.Model.CurrentPlayArray;
import com.db.musicplayerdb.Model.FavCode;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.databinding.ActivityMusicBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MusicActivity extends AppCompatActivity {
    public static ActivityMusicBinding binding;
    RotateAnimation rotateAnimation;
    public static int protime;
    public static SeekBar seekbar;
    public static Boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        seekbar = findViewById(R.id.linearProgressIndicator);

        isCreated = true;

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        String lastplayed = preferences.getString("LastMusicPlay", "");

        if (!lastplayed.equals("")) {
            if (lastplayed.equals(CurrentMusic.get(CurrentPosition).getName())) {
                ResumeSongPlay();
            } else {
                newSongPlay();
            }
        } else {
            newSongPlay();
        }

        setnewsongdata();

        if (!FavoriteActivity.Favmusic.isEmpty()) {
            FavoriteActivity.Favmusic = FavCode.loadArrayListFromSharedPreferences(MusicActivity.this);

            String name1 = CurrentMusic.get(CurrentPosition).getName();
            for (int i = 0; i < FavoriteActivity.Favmusic.size(); i++) {
                String name2 = FavoriteActivity.Favmusic.get(i).getName();

                if (name1.equals(name2)) {
                    binding.imageView9.setImageResource(R.drawable.fillheart);
                }
            }

        }

        if(CurrentMode==1){
            binding.re1.setVisibility(View.VISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);
        }
        if(CurrentMode==2){
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.VISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);
        }
        if(CurrentMode==3){
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.VISIBLE);
        }

        binding.imageView9.setOnClickListener(v -> {
            MusicModel model = new MusicModel(CurrentMusic.get(CurrentPosition).getName(), CurrentMusic.get(CurrentPosition).getPath(), CurrentMusic.get(CurrentPosition).getDuration(), CurrentMusic.get(CurrentPosition).getArtist(), CurrentMusic.get(CurrentPosition).getPlayCount());

            if (!FavoriteActivity.Favmusic.isEmpty()) {
                String name1 = CurrentMusic.get(CurrentPosition).getName();
                for (int i = 0; i < FavoriteActivity.Favmusic.size(); i++) {
                    String name2 = FavoriteActivity.Favmusic.get(i).getName();

                    if (name1.equals(name2)) {
                        FavoriteActivity.Favmusic.remove(i);
                        binding.imageView9.setImageResource(R.drawable.noheart);
                        Toast.makeText(this, "Already in Favorite...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            FavoriteActivity.Favmusic.add(model);
            binding.imageView9.setImageResource(R.drawable.fillheart);

            FavCode.favdata(MusicActivity.this, FavoriteActivity.Favmusic);

        });

        binding.play.setOnClickListener(v -> {

            binding.pause.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.INVISIBLE);

            if (FirstPlay == 0) {
                newSongPlay();
            } else {
                mainmediaPlayer.start();
            }
            updateAllBottom();
            binding.imageround.startAnimation(rotateAnimation);
        });
        binding.pause.setOnClickListener(v -> {

            binding.pause.setVisibility(View.INVISIBLE);
            binding.play.setVisibility(View.VISIBLE);
            if (mainmediaPlayer.isPlaying()) {
                mainmediaPlayer.pause();
            }
            updateAllBottom();
            binding.imageround.clearAnimation();
        });

        binding.re1.setOnClickListener(v -> {
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.VISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode = 2;
        });
        binding.re2.setOnClickListener(v -> {
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.VISIBLE);

            mainmediaPlayer.setLooping(true);
            SplashActivity.CurrentMode = 3;
        });
        binding.re3.setOnClickListener(v -> {
            binding.re1.setVisibility(View.VISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode = 1;
        });

        binding.queue.setOnClickListener(v -> {
            startActivity(new Intent(MusicActivity.this, QueueActivity.class));
        });

        binding.imageView12.setOnClickListener(v -> {
            if (CurrentPosition < CurrentMusic.size() - 1) {
                CurrentPosition++;
                newSongPlay();
            }
        });
        binding.imageView11.setOnClickListener(v -> {
            if (CurrentPosition > 0) {
                CurrentPosition--;
                newSongPlay();
            }
        });


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // This method is called when the user drags the SeekBar thumb

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // This method is called when the user starts dragging the SeekBar
//                mainmediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CurrentPlayingTime.progress = seekBar.getProgress();
                mainmediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(CurrentPlayingTime.progress));
//                mainmediaPlayer.start();
            }
        });


    }

    private void ResumeSongPlay() {

        if (FirstPlay == 0) {
            newSongPlay();
            CurrentMode=1;
        } else {
            //palying thayelu hoy to

            protime = Integer.parseInt(CurrentMusic.get(CurrentPosition).getDuration()) / 1000;
            seekbar.setMax(protime);
            seekbar.setProgress(CurrentPlayingTime.progress);

            String formattedTime = formatSecondsToHHMMSS(CurrentPlayingTime.progress);
            binding.textView6.setText("" + formattedTime);

        }

    }

    private void newSongPlay() {

        if(CurrentMode==1){
            binding.re1.setVisibility(View.VISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);
        }
        if(CurrentMode==2){
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.VISIBLE);
            binding.re3.setVisibility(View.INVISIBLE);
        }
        if(CurrentMode==3){
            binding.re1.setVisibility(View.INVISIBLE);
            binding.re2.setVisibility(View.INVISIBLE);
            binding.re3.setVisibility(View.VISIBLE);
        }

        try {
            CurrentPlayingTime.progress = 0;

            protime = Integer.parseInt(CurrentMusic.get(CurrentPosition).getDuration()) / 1000;

            seekbar.setProgress(0);
            seekbar.setMax(protime);

            mainmediaPlayer.reset();
            mainmediaPlayer.setDataSource(CurrentMusic.get(CurrentPosition).getPath());
            mainmediaPlayer.prepare();
            mainmediaPlayer.start();

            editor.putString("LastMusicPlay", CurrentMusic.get(CurrentPosition).getName());
            editor.commit();

            LastPlayCode.Lastdata(MusicActivity.this, CurrentMusic);

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

            FirstPlay = 1;
            setnewsongdata();
            updateAllBottom();

            mainmediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    CurrentPlayingTime.progress = 0;
                    seekbar.setProgress(0);
                    binding.pause.setVisibility(View.INVISIBLE);
                    binding.play.setVisibility(View.VISIBLE);
                    binding.textView6.setText("00:00");
                    updateAllBottom();

                    if (CurrentPosition < CurrentMusic.size() - 1) {
                        if (CurrentMusic.size() != 1) {

                            if(CurrentMode==1){
                                Random random = new Random();
                                int randomNumber = random.nextInt(CurrentMusic.size());
                                CurrentPosition = randomNumber;
                                newSongPlay();
                                updateAllBottom();
                            }
                            if(CurrentMode==2){
                                CurrentPosition++;
                                newSongPlay();
                                updateAllBottom();
                            }
                            if(CurrentMode==3){
                                newSongPlay();
                                updateAllBottom();
                            }


                        } else {
                            binding.play.setVisibility(View.VISIBLE);
                            binding.pause.setVisibility(View.INVISIBLE);
                            binding.imageround.clearAnimation();
                            FirstPlay = 0;

                            updateAllBottom();

                            binding.play.setOnClickListener(v -> {
                                binding.pause.setVisibility(View.VISIBLE);
                                binding.play.setVisibility(View.INVISIBLE);

                                if (FirstPlay == 0) {
                                    newSongPlay();
                                } else {
                                    mainmediaPlayer.start();
                                }
                                updateAllBottom();
                                binding.imageround.startAnimation(rotateAnimation);
                            });
                            binding.pause.setOnClickListener(v -> {
                                binding.pause.setVisibility(View.INVISIBLE);
                                binding.play.setVisibility(View.VISIBLE);
                                if (mainmediaPlayer.isPlaying()) {
                                    mainmediaPlayer.pause();
                                }
                                updateAllBottom();
                                binding.imageround.clearAnimation();
                            });
                        }
                    } else {

                    }
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void playAudio(MusicModel audio) {

        String name1 = audio.getName();

        for (int i = 0; i < MostPlayMusic.size(); i++) {
            String name2 = MostPlayMusic.get(i).getName();

            if (name1.equals(name2)) {
                MostPlayMusic.get(i).incrementPlayCount();
            }
        }

        MostplayCode.mostplaylistdata(MusicActivity.this, MostPlayMusic);
        SplashActivity.editor.putInt("mostplay", 1);
        SplashActivity.editor.commit();


    }

    private void setnewsongdata() {
        binding.name.setText("" + CurrentMusic.get(CurrentPosition).getName());
        binding.folder.setText("" + CurrentMusic.get(CurrentPosition).getArtist());

        String date1 = ThubnailGet.formatDuration(Long.parseLong(CurrentMusic.get(CurrentPosition).getDuration()));
        binding.endtime.setText("" + date1);

        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(30000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        if (mainmediaPlayer.isPlaying()) {
            binding.pause.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.INVISIBLE);
            binding.imageround.startAnimation(rotateAnimation);
        } else {
            binding.pause.setVisibility(View.INVISIBLE);
            binding.play.setVisibility(View.VISIBLE);
            binding.imageround.clearAnimation();
        }

    }

    public static void updateSeekbar() {
        protime = Integer.parseInt(CurrentMusic.get(CurrentPosition).getDuration()) / 1000;

        if (MainActivity.mainmediaPlayer.isLooping()) {
            if (seekbar.getProgress() == protime) {
                CurrentPlayingTime.progress = 0;
                binding.textView6.setText("00:00");
            }
        }

        seekbar.setProgress(CurrentPlayingTime.progress);
        String formattedTime = formatSecondsToHHMMSS(CurrentPlayingTime.progress);
        binding.textView6.setText("" + formattedTime);

    }


    public static String formatSecondsToHHMMSS(int seconds) {

        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return String.format("%02d:%02d", minutes, remainingSeconds);
    }


}