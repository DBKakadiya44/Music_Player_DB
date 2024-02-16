package com.db.musicplayerdb.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MostplayCode
{
    public static void mostplaylistdata(Context context, ArrayList<MusicModel> arrayList){

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        savemostplaylistSharedPreferences(context,"MOSTPLAY", "mostplayarray", json);

    }

    public static void savemostplaylistSharedPreferences(Context context,String preferencesName, String key, String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static void loadmostplaylistSharedPreferences(Context context) {
//        CurrentPlayArray.MostPlayMusic.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MOSTPLAY", Context.MODE_PRIVATE);
        String storedJson = sharedPreferences.getString("mostplayarray", "");
        Gson gson = new Gson();
        CurrentPlayArray.MostPlayMusic =  gson.fromJson(storedJson, new TypeToken<ArrayList<MusicModel>>() {}.getType());
    }
}
