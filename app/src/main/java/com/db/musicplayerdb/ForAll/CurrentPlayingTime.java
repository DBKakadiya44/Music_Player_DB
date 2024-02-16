package com.db.musicplayerdb.ForAll;

import static com.db.musicplayerdb.MainActivity.mainmediaPlayer;

import android.os.Handler;
import android.util.Log;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.MusicActivity;

public class CurrentPlayingTime
{
    public static int progress = 0;

    public static void updateProgress(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.mainmediaPlayer.isPlaying()) {
                    progress++;
                    Log.d("IHGFY", "handleMessage: SongFragment Time = "+progress);

                    if(MusicActivity.isCreated){
                        MusicActivity.updateSeekbar();
                    }
                }
                updateProgress();
            }
        },1000);
    }
}
