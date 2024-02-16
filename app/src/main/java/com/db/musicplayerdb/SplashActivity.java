package com.db.musicplayerdb;

import static com.db.musicplayerdb.Model.CurrentPlayArray.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


import com.db.musicplayerdb.Model.CurrentPlayArray;
import com.db.musicplayerdb.Model.LastPlayCode;
import com.db.musicplayerdb.Model.PlayListNamePref;
import com.db.musicplayerdb.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static int FirstPlay = 0;
    public static int CurrentMode = 1;
    public static int playlisttrue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        preferences = getSharedPreferences("MyDATA", MODE_PRIVATE);
        editor = preferences.edit();


        playlisttrue = preferences.getInt("playlist",0);

        checkLastPlayedMusic();

        PlayListNamePref.PlayListNamePref(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.getInt("screen", 0) == 0) {
                    startActivity(new Intent(SplashActivity.this, PrivacyActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, 3000);

    }

    private void checkLastPlayedMusic() {
        String latsongname = preferences.getString("LastMusicPlay", "");
        if (!latsongname.equals("")) {
            LastPlayCode.loadLastArrayListFromSharedPreferences(SplashActivity.this);

            for (int i = 0; i < CurrentMusic.size(); i++) {
                String name = CurrentMusic.get(i).getName();

                if (latsongname.equals(name)) {
                    CurrentPosition = i;
                    break;
                }
            }
        }
    }
}