package com.db.musicplayerdb.Model;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PlayListNamePref {

    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_STRING_LIST = "stringList";

    public static SharedPreferences sharedPreferences;
    public static Gson gson;

    public static void PlayListNamePref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static void saveStringList(List<String> stringList) {
        String serializedList = gson.toJson(stringList);
        sharedPreferences.edit().putString(KEY_STRING_LIST, serializedList).apply();
    }

    public static List<String> getStringList() {
        String serializedList = sharedPreferences.getString(KEY_STRING_LIST, null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(serializedList, type);
    }
}
