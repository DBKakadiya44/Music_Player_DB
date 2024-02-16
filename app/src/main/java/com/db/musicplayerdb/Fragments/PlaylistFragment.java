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
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.db.musicplayerdb.Adapter.FolderAdapter;
import com.db.musicplayerdb.Adapter.MostPlayAdapter;
import com.db.musicplayerdb.Adapter.PlayListAdapter;
import com.db.musicplayerdb.Adapter.SelectAdapter;
import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.ForAll.ThubnailGet;
import com.db.musicplayerdb.ForAll.UpdateBottom;
import com.db.musicplayerdb.Fragments.Activity.FavoriteActivity;
import com.db.musicplayerdb.Fragments.Activity.LastAddActivity;
import com.db.musicplayerdb.Fragments.Activity.MostPlayActivity;
import com.db.musicplayerdb.Fragments.Activity.RecentActivity;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.CurrentPlayArray;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.MostplayCode;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.Model.PlayListCode;
import com.db.musicplayerdb.Model.PlayListNamePref;
import com.db.musicplayerdb.MusicActivity;
import com.db.musicplayerdb.QueueActivity;
import com.db.musicplayerdb.R;
import com.db.musicplayerdb.SplashActivity;
import com.db.musicplayerdb.databinding.FragmentPlaylistBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaylistFragment extends Fragment {
    public static FragmentPlaylistBinding binding;
    public static MainActivity mainActivity;
    public static Context context;
    public static Boolean isCreated = false;
    public static List<String> namelist = new ArrayList<>();

    public static ArrayList<MusicModel> playlistwisemusic = new ArrayList();
    TextView f2;

    public PlaylistFragment(MainActivity mainActivity, TextView f2) {
        this.mainActivity = mainActivity;
        this.f2 = f2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(getLayoutInflater());

        f2.setTextColor(getActivity().getColor(R.color.maincolor));
        f2.setTextSize(16);

        context = mainActivity;
        isCreated = true;
        UpdateBottom.updateAllBottom();

        checkrecycler();

        binding.addplaylist.setOnClickListener(v -> {
            showCustomDialog();
        });

        binding.m1.setOnClickListener(v -> {
            startActivity(new Intent(mainActivity, FavoriteActivity.class));
        });
        binding.m2.setOnClickListener(v -> {
            startActivity(new Intent(mainActivity, RecentActivity.class));
        });
        binding.m3.setOnClickListener(v -> {
            startActivity(new Intent(mainActivity, LastAddActivity.class));
        });
        binding.m4.setOnClickListener(v -> {
            startActivity(new Intent(mainActivity, MostPlayActivity.class));
        });

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

            });
            binding.bpause.setOnClickListener(v -> {
                binding.bplay.setVisibility(View.VISIBLE);
                binding.bpause.setVisibility(View.INVISIBLE);
                mainmediaPlayer.pause();

                UpdateBottom.updateAllBottom();

            });
            //current playing queue activity
            binding.bqueue.setOnClickListener(v -> {
                context.startActivity(new Intent(context , QueueActivity.class));
            });

        }
        // for music animation update


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

    public static void checkrecycler() {

        int sharetrue = SplashActivity.preferences.getInt("playlist", 0);

        if (sharetrue == 1) {
            namelist = PlayListNamePref.getStringList();
        }

        if (namelist.isEmpty()) {

        } else {
            PlayListAdapter adapter = new PlayListAdapter(mainActivity, namelist);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
            binding.playlistrecycler.setLayoutManager(manager);
            binding.playlistrecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void showCustomDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customView);
        builder.setCancelable(false);
        AlertDialog customDialog = builder.create();

        CardView cancel = customView.findViewById(R.id.cancel);
        CardView confirm = customView.findViewById(R.id.confirm);
        EditText editText = customView.findViewById(R.id.editfield);

        cancel.setOnClickListener(v -> {
            customDialog.dismiss();
        });

        confirm.setOnClickListener(v -> {
            customDialog.dismiss();
            String edit = editText.getText().toString();
            showselectdialoge(edit);
        });
        customDialog.show();
    }

    private void showselectdialoge(String edit) {

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(mainActivity , SongsFragment.musicdata, edit);
        bottomSheetFragment.show(mainActivity.getSupportFragmentManager(), bottomSheetFragment.getTag());


    }




}