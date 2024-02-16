package com.db.musicplayerdb.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class LastPlayCode
{
    public static void Lastdata(Context context, ArrayList<MusicModel> arrayList){

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        saveLastArrayListToSharedPreferences(context,"LASTDATA", "LASTLIST", json);

    }

    public static void saveLastArrayListToSharedPreferences(Context context,String preferencesName, String key, String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static void loadLastArrayListFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LASTDATA", Context.MODE_PRIVATE);
        String storedJson = sharedPreferences.getString("LASTLIST", "");
        Gson gson = new Gson();
        CurrentPlayArray.CurrentMusic =  gson.fromJson(storedJson, new TypeToken<ArrayList<MusicModel>>() {}.getType());
    }

}
