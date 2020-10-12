package com.example.altayeb.injaz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Altayeb on 1/12/2018.
 */
public class SharePref extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "Ingaz";

    public static void saveUserInSharePref(String phone_no, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("phone_no", phone_no);
        edit.apply();
        Toast.makeText(context, "save login data in sharePref done", Toast.LENGTH_SHORT).show();
    }
    public static void save_full_name(String full_name, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("full_name", full_name);
        edit.apply();
        Toast.makeText(context, "save Full_name sharePref done", Toast.LENGTH_SHORT).show();
    }

    //
    public static boolean isLoggedin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        boolean x = false;
        String name = sp.getString("phone_no", null);
        if (name != null) {
            x = true;
        }
        return x;
    }

    public static String get_phone_no(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("phone_no", null);
    }

    public static void logout(Context context) {
        final SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }

    public static void save_profile(String phone_no, String gender, String dept, String about, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("phone_no", phone_no);
        edit.putString("gender", gender);
        edit.putString("dept", dept);
        edit.putString("about", about);
        edit.apply();
        Toast.makeText(context, "save profile data in sharePref done", Toast.LENGTH_SHORT).show();
    }

    public static boolean isMyprofileSaved(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        boolean x = false;
        String gender = sp.getString("gender", null);
        if (gender != null) {
            x = true;
        }
        return x;
    }

    public static String get_gender(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("gender", null);
    }

    public static String get_dept(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("dept", null);
    }

    public static String get_about(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("about", null);
    }

    public static String get_full_name(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("full_name", null);
    }
/*
//location
public static void saveLocation(String lattitude,String longitude, Context context) {
    SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sp.edit();
    edit.putString("lattitude", lattitude);
    edit.putString("longitude", longitude);
    edit.apply();
    Toast.makeText(context, "save Location data done", Toast.LENGTH_SHORT).show();
}
    public static String get_lattitude(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("lattitude", null);
    }
    public static String get_longitude(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        return sp.getString("longitude", null);
    }
    */
}

