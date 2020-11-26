package com.budyfriend_code.song.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class preferences {
    private static String key_value = "id";
    private static String username_value = "username";
    private static String active_value = "active";
    private static String level_value = "level";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setId(Context context, String id){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key_value,id);
        editor.apply();
    }

    public static String getId(Context context){
        return getSharedPreferences(context).getString(key_value,"");
    }

    public static void setUsername(Context context, String user){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(username_value,user);
        editor.apply();
    }

    public static String getUsername(Context context){
        return getSharedPreferences(context).getString(username_value,"");
    }

    public static void setLevel(Context context, String level){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(level_value,level);
        editor.apply();
    }

    public static String getLevel(Context context){
        return getSharedPreferences(context).getString(level_value,"");
    }

    public static void setActive(Context context, boolean active){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(active_value,active);
        editor.apply();
    }

    public static boolean getActive(Context context){
        return getSharedPreferences(context).getBoolean(active_value,false);
    }

    public static void clearData(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }


}
