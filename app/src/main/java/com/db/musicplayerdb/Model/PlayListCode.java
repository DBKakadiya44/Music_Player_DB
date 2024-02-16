package com.db.musicplayerdb.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.db.musicplayerdb.Fragments.PlaylistFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PlayListCode
{
    public static void playlistdata(Context context, String listname, ArrayList<MusicModel> arrayList){

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        saveplaylistSharedPreferences(context,"PLAYLIST", listname, json);

    }

    public static void saveplaylistSharedPreferences(Context context,String preferencesName, String key, String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static void loadplaylistSharedPreferences(Context context,String listname) {
        PlaylistFragment.playlistwisemusic.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences("PLAYLIST", Context.MODE_PRIVATE);
        String storedJson = sharedPreferences.getString(listname, "");
        Gson gson = new Gson();
        PlaylistFragment.playlistwisemusic =  gson.fromJson(storedJson, new TypeToken<ArrayList<MusicModel>>() {}.getType());
    }

}
