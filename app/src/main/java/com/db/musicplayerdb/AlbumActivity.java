package com.db.musicplayerdb;

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
import com.db.musicplayerdb.Adapter.AcAlbumAdapter;
import com.db.musicplayerdb.Adapter.AcAlbumAdapter2;
import com.db.musicplayerdb.Adapter.AcAlbumAdapter3;
import com.db.musicplayerdb.Adapter.AcAlbumAdapter4;
import com.db.musicplayerdb.Adapter.LastAddAdapter;
import com.db.musicplayerdb.Adapter.MostPlayAdapter;
import com.db.musicplayerdb.Adapter.RecentAdapter;
import com.db.musicplayerdb.Adapter.SongAdapter;
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
import com.db.musicplayerdb.Model.CurrentPlayArray;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.databinding.ActivityAlbumBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AlbumActivity extends AppCompatActivity {
    public static ActivityAlbumBinding binding;
    public static int from;
    public static Context context;
    public static Boolean isCreated = false;
    public static ArrayList<MusicModel> musicdata = new ArrayList<>();
    public static Boolean isplaying = false;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        context = AlbumActivity.this;
        isCreated = true;

        from = getIntent().getIntExtra("from",1);
        String name = getIntent().getStringExtra("name");

        binding.title.setText(""+name);

        if(from==1){
            musicdata = FoldersFragment.folderwisemusic;
        }
        if(from==2){
            musicdata = AlbumsFragment.albumwisemusic;
        }
        if(from==3){
            musicdata = ArtistFragment.artistwisemusic;
        }
        if(from==4){
            musicdata = PlaylistFragment.playlistwisemusic;
        }

        if(musicdata.isEmpty()){
            binding.noText.setVisibility(View.VISIBLE);
            binding.textView5.setText("0 Songs");
        }else {
            binding.noText.setVisibility(View.INVISIBLE);
            binding.textView5.setText(musicdata.size()+" Songs");
            adapter1();
        }

        UpdateBottom.updateAllBottom();

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

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.shuffle.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.INVISIBLE);
            binding.loop.setVisibility(View.VISIBLE);
            binding.oneloop.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode=2;
        });
        binding.loop.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.INVISIBLE);
            binding.loop.setVisibility(View.INVISIBLE);
            binding.oneloop.setVisibility(View.VISIBLE);

            mainmediaPlayer.setLooping(true);
            SplashActivity.CurrentMode=3;
        });
        binding.oneloop.setOnClickListener(v -> {
            binding.shuffle.setVisibility(View.VISIBLE);
            binding.loop.setVisibility(View.INVISIBLE);
            binding.oneloop.setVisibility(View.INVISIBLE);

            mainmediaPlayer.setLooping(false);
            SplashActivity.CurrentMode=1;
        });

    }

    public static void adapter1() {
        AcAlbumAdapter adapter = new AcAlbumAdapter(context , musicdata);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        binding.albumAcrecycler.setLayoutManager(manager);
        binding.albumAcrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            isplaying = true;

            if (mainmediaPlayer.isLooping()) {

                try {
                    mainmediaPlayer.reset();
                    mainmediaPlayer.setDataSource(musicdata.get(currentIndex).getPath());
                    mainmediaPlayer.prepare();
                    mainmediaPlayer.start();

                    SplashActivity.editor.putString("LastMusicPlay", musicdata.get(currentIndex).getName());
                    SplashActivity.editor.commit();

                    LastPlayCode.Lastdata(context , CurrentMusic);


                    CurrentPosition = currentIndex;

                    UpdateBottom.updateAllBottom();
                    adapter1();


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

                        LastPlayCode.Lastdata(context , CurrentMusic);

                        CurrentPosition = currentIndex;

                        UpdateBottom.updateAllBottom();
                        adapter1();


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
        }else {
            if (mainmediaPlayer.isLooping()) {

                try {
                    mainmediaPlayer.reset();
                    mainmediaPlayer.setDataSource(musicdata.get(currentIndex).getPath());
                    mainmediaPlayer.prepare();
                    mainmediaPlayer.start();

                    SplashActivity.editor.putString("LastMusicPlay", musicdata.get(currentIndex).getName());
                    SplashActivity.editor.commit();

                    LastPlayCode.Lastdata(context , CurrentMusic);


                    CurrentPosition = currentIndex;

                    UpdateBottom.updateAllBottom();
                    adapter1();


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

                        LastPlayCode.Lastdata(context , CurrentMusic);

                        CurrentPosition = currentIndex;

                        UpdateBottom.updateAllBottom();
                        adapter1();


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
                if(isplaying){
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
                adapter1();
            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                if(isplaying){
                    binding.playall.setVisibility(View.VISIBLE);
                    binding.pauseall.setVisibility(View.INVISIBLE);
                }

                UpdateBottom.updateAllBottom();
                adapter1();
            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context, QueueActivity.class));
            });

        }
        // for music animation update
       adapter1();

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