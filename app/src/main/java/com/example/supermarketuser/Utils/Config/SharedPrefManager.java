package com.example.supermarketuser.Utils.Config;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.supermarketuser.Utils.Config.Constants.IS_LOGIN;
import static com.example.supermarketuser.Utils.Config.Constants.USER_SKIP;

public class SharedPrefManager {
    SharedPreferences preferences;
    public static final String PREF_TOTAL_KEY = "pref_total_key";
    public static final String PREF_NAME = "pref_total_key";
    Context context;

    public SharedPrefManager(Context context) {
        this.context = context;

    }

    public boolean isLogedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public boolean isSkip() {
        return preferences.getBoolean(USER_SKIP, false);

    }

    public static void SaveTotalKey(Context context, int total) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_TOTAL_KEY, total);
        editor.apply();
    }

    public static int loadFrompref(Context context) {
        SharedPreferences rpref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return rpref.getInt(PREF_TOTAL_KEY, 0);

    }

    public static void registerPrif(Context context, SharedPreferences.OnSharedPreferenceChangeListener lisner) {
        SharedPreferences rpref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        rpref.registerOnSharedPreferenceChangeListener(lisner);
    }

    public static void unregisterPref(Context context, SharedPreferences.OnSharedPreferenceChangeListener lisner){
        SharedPreferences rpref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        rpref.unregisterOnSharedPreferenceChangeListener(lisner);
    }


}
