package com.example.mk.client;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utility {
    final static String base_url = "https://contact-api-swart.vercel.app/";
    private static final String PREFERENCE_FILE_KEY = Utility.class.getPackage().getName();
    static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl((base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setValue(Context context, String xPref, String xValue){ //9
        SharedPreferences sp= context.getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(xPref,xValue);
        editor.commit();
    }
    public static String getValue(Context context , String xPref){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        String xValue = sp.getString(xPref,null);
        return xValue;

    }

    public static boolean checkValue(Context context,String xPref){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        String xValue = sp.getString(xPref,null);
        return xValue != null;
    }

    public static void clearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("xUsername",null);
        editor.apply();


    }
}
