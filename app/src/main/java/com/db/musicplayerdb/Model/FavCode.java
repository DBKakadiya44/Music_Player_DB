package com.db.musicplayerdb.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class FavCode
{
    public static void favdata(Context context,ArrayList<MusicModel> arrayList){

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        saveArrayListToSharedPreferences(context,"FAVDATA", "FAVLIST", json);

    }

    public static void saveArrayListToSharedPreferences(Context context,String preferencesName, String key, String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<MusicModel> loadArrayListFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FAVDATA", Context.MODE_PRIVATE);
        String storedJson = sharedPreferences.getString("FAVLIST", "");
        Gson gson = new Gson();
        return gson.fromJson(storedJson, new TypeToken<ArrayList<MusicModel>>() {}.getType());
    }

}
