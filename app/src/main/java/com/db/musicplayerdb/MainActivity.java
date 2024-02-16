package com.db.musicplayerdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.db.musicplayerdb.ForAll.CurrentPlayingTime;
import com.db.musicplayerdb.Fragments.AlbumsFragment;
import com.db.musicplayerdb.Fragments.ArtistFragment;
import com.db.musicplayerdb.Fragments.FoldersFragment;
import com.db.musicplayerdb.Fragments.PlaylistFragment;
import com.db.musicplayerdb.Fragments.SongsFragment;
import com.db.musicplayerdb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static MediaPlayer mainmediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        mainmediaPlayer = new MediaPlayer();
        CurrentPlayingTime.updateProgress();

        replaceFragment(new SongsFragment(MainActivity.this,binding.f1));

        binding.cancel.setOnClickListener(v -> {
            replaceFragment(new SongsFragment(MainActivity.this,binding.f1));
        });
        binding.exit.setOnClickListener(v -> {
            finishAffinity();
        });

        binding.f1.setOnClickListener(v -> {
            setallcolor();
            binding.f1.setTextColor(getColor(R.color.maincolor));
            binding.f1.setTextSize(16);
            replaceFragment(new SongsFragment(MainActivity.this,binding.f1));
        });
        binding.f2.setOnClickListener(v -> {
            setallcolor();
            binding.f2.setTextColor(getColor(R.color.maincolor));
            binding.f2.setTextSize(16);
            replaceFragment(new PlaylistFragment(MainActivity.this,binding.f2));
        });
        binding.f3.setOnClickListener(v -> {
            setallcolor();
            binding.f3.setTextColor(getColor(R.color.maincolor));
            binding.f3.setTextSize(16);
            replaceFragment(new FoldersFragment(MainActivity.this,binding.f3));
        });
        binding.f4.setOnClickListener(v -> {
            setallcolor();
            binding.f4.setTextColor(getColor(R.color.maincolor));
            binding.f4.setTextSize(16);
            replaceFragment(new ArtistFragment(MainActivity.this,binding.f4));
        });
        binding.f5.setOnClickListener(v -> {
            setallcolor();
            binding.f5.setTextColor(getColor(R.color.maincolor));
            binding.f5.setTextSize(16);
            replaceFragment(new AlbumsFragment(MainActivity.this,binding.f5));
        });

    }

    private void setallcolor() {
        binding.f1.setTextColor(getColor(R.color.white));
        binding.f2.setTextColor(getColor(R.color.white));
        binding.f3.setTextColor(getColor(R.color.white));
        binding.f4.setTextColor(getColor(R.color.white));
        binding.f5.setTextColor(getColor(R.color.white));

        binding.f1.setTextSize(12);
        binding.f2.setTextSize(12);
        binding.f3.setTextSize(12);
        binding.f4.setTextSize(12);
        binding.f5.setTextSize(12);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framel, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        setallcolor();
        super.onBackPressed();
    }

}